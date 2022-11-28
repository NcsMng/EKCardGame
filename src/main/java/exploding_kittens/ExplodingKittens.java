package exploding_kittens;
import exploding_kittens.managers.CardEffectManager;
import exploding_kittens.managers.PlayerManager;
import exploding_kittens.managers.TurnManager;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;
import exploding_kittens.model.cards.Card;
import exploding_kittens.model.decks.MainDeck;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ExplodingKittens {

    private MainDeck mainDeck;
    private PlayerManager playerManager;
    private TurnManager turnManager;

    private CardEffectManager cardEffectManager;

    public ExplodingKittens(String[] params) throws Exception {
        if (params.length == 2) {
            int numberOfPlayers = Integer.parseInt(params[0]);
            int numberOfBots = 0;

            if (StringUtils.isNotBlank(params[1])) {
                numberOfBots = Integer.parseInt(params[1]);
            }
            MainDeck.tearDown();
            mainDeck = MainDeck.getInstance();
            cardEffectManager = CardEffectManager.getInstance();
            turnManager = TurnManager.getInstance();
            playerManager = PlayerManager.getInstance();
            this.initGame(numberOfPlayers, numberOfBots);
            this.initGame(Integer.parseInt(params[0]), Integer.parseInt(params[1]));
        } else if (params.length == 1) {
            this.client(params[0]);
        } else {
            System.out.println("Server syntax: java ExplodingKittens numPlayers numBots");
            System.out.println("Client syntax: IP");
        }
    }

    public void initGame(int numPlayers, int numBots) {
        try {
            try (ServerSocket serverSocket = new ServerSocket(2048)) {
                playerManager.addPlayers(numPlayers, numBots, serverSocket);

                List<Player> players = playerManager.getPlayers();
                mainDeck.initStartingDeck();
                playerManager.makePlayerDrawInitialHand();
                mainDeck.populateDeck(numPlayers);
                turnManager.setPlayerManager(playerManager);

                Random rnd = new Random();
                game(rnd.nextInt(players.size()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void game(int startPlayer) throws Exception {
        Player activePlayer = turnManager.changeActivePlayer(startPlayer);
        List<Player> players = turnManager.getTurnOrder();
        do {
            System.out.println("TURN OF PLAYER : " + activePlayer.playerID);
            activePlayer.sendMessage("It is your turn");
            for (Player p : players) {
                if (p != activePlayer) {
                    p.sendMessage("It is now the turn of player " + activePlayer.playerID);
                }
            }

            long turnsLeft = turnManager.getTurnsLeftToPlayCurrentPlayer();
            activePlayer.sendMessage("\nYou have " + turnsLeft + ((turnsLeft > 1) ? " turns" : " turn") + " to take");
            activePlayer.sendMessage("Your hand: " + activePlayer.getHand());

            String response = getActionFromPlayer();

            switch (StringUtils.capitalize(response.split(" ")[0].trim())) {
                case "Pass" -> turnManager.drawAndEndTurn();
                case "Two" -> {
                    String[] args = response.split(" ");
                    cardEffectManager.makeDoubleCardSpecialCombo(Integer.parseInt(args[2]), args[1]);
                }
                case "Three" -> {
                    String[] args = response.split(" ");
                    cardEffectManager.makeTripleCardSpecialCombo(Integer.parseInt(args[2]), args[1]);
                }

                case "AttackCard" -> cardEffectManager.useAttackCardEffect();

                case "FavorCard" -> {
                    String[] args = response.split(" ");
                    cardEffectManager.useFavorCardEffect(activePlayer, Integer.parseInt(args[1]));
                }
                case "ShuffleCard" -> cardEffectManager.useShuffleCardEffect();
                case "SkipCard" -> cardEffectManager.useSkipCardEffect();
                case "SeeTheFutureCard" -> cardEffectManager.useSeeTheFutureCardEffect();
                default -> activePlayer.sendMessage("Not a viable option, try again");
            }
            activePlayer = turnManager.getCurrentPlayer();
        } while (!turnManager.getTurnOrder().stream().allMatch(player -> player.playerID == turnManager.getCurrentPlayer().playerID));

        for (Player player : playerManager.getPlayers()) {
            player.sendMessage("Player " + activePlayer.playerID + " has won the game");
        }
        System.exit(0);
    }

    private String getActionFromPlayer() {
        Player activePlayer = turnManager.getCurrentPlayer();
        String otherPlayerIDs = playerManager.getAllPlayersIDs()
                .stream().filter(integer -> integer != activePlayer.playerID)
                .map(integer -> integer + "")
                .reduce("", (s, integer) -> s + " " + integer);

        StringBuilder yourOptions = new StringBuilder("You have the following OPTIONS:\n");
        List<Card> playerHand = activePlayer.getHand();

        Map<Integer, Long> countedCards = playerHand.stream().collect(Collectors.groupingBy(Card::getID, Collectors.counting()));
        countedCards.forEach((cardId, cardCounted) -> {
            Class<? extends Card> card = CardFactory.cardsIdMap.get(cardId);
            String cardName = StringUtils.substringAfterLast(card.getName(), '.');
            int valueCounted = cardCounted.intValue();
            if (valueCounted >= 3) {
                yourOptions.append("\tThree ").append(cardName).append(" [target] (available targets: ").append(otherPlayerIDs).append(") (Name and pick a card)\n");
            }
            if (valueCounted >= 2) {
                yourOptions.append("\tTwo ").append(cardName).append(" [target] (available targets: ").append(otherPlayerIDs).append(") (Steal random card)\n");
            }

            if (valueCounted == 1) {
                if (cardId == CardFactory.FAVOR_CARD) {
                    yourOptions.append("\t").append(cardName).append(" [target] (available targets:").append(otherPlayerIDs).append(")\n");
                } else if (cardId != CardFactory.DEFUSE_CARD) {
                    yourOptions.append("\t").append(cardName).append("\n");
                }
            }
        });
        yourOptions.append("\tPass\n");
        activePlayer.sendMessage(yourOptions.toString());
        return activePlayer.readMessage();
    }

    public void client(String ip) throws Exception {
        //Connect to server
        Socket aSocket = new Socket(ip, 2048);
        ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
        outToServer.writeObject("Player Connected");

        ExecutorService threadpool = Executors.newFixedThreadPool(1);
        Runnable receive = () -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    String nextMessage = (String) inFromServer.readObject();
                    System.out.println(nextMessage);
                    if (nextMessage.contains("OPTIONS") || nextMessage.contains("FAVOR") || nextMessage.contains("EXPLODINGKITTEN") || nextMessage.contains("THREECARDCOMBO") || nextMessage.contains("CARDNOTFOUND") || nextMessage.contains("PLAYERNOTFOUND")) { //options (your turn), Give (Opponent played Favor), Where (You defused an exploding kitten)
                        outToServer.writeObject(br.readLine());
                    } else if (nextMessage.contains("<Enter>")) { //Press <Enter> to play Nope and Interrupt
                        int millisecondsWaited = 0;
                        while (!br.ready() && millisecondsWaited < (5 * 1000)) {
                            Thread.sleep(200);
                            millisecondsWaited += 200;
                        }
                        if (br.ready()) {
                            outToServer.writeObject(br.readLine());
                        } else
                            outToServer.writeObject(" ");
                    }
                } catch (Exception e) {
                    System.exit(0);
                }
            }
        };
        threadpool.execute(receive);
    }

    public static void main(String[] args) throws Exception {

        new ExplodingKittens(args);

    }
}
