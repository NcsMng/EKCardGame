package exploding_kittens.managers;

import exploding_kittens.exceptions.CardNotFoundException;
import exploding_kittens.exceptions.InvalidComboException;
import exploding_kittens.exceptions.NoCardsToMoveException;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;
import exploding_kittens.model.cards.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TurnManager {

    private static TurnManager turnManager;
    private Player currentPlayer;
    private PlayerManager playerManager;
    private final List<Player> turnOrder;

    public static TurnManager getInstance() {
        if (turnManager == null) {
            turnManager = new TurnManager();
        }
        return turnManager;
    }

    public static void tearDown() {
        turnManager = null;
    }

    private TurnManager() {
        turnOrder = new ArrayList<>();
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setPlayerManager(PlayerManager pm) {
        Player temp = currentPlayer;
        playerManager = pm;
        List<Player> players = playerManager.getPlayers();
        turnOrder.addAll(players);
        if (temp == null) {
            currentPlayer = turnOrder.get(0);
        } else {
            currentPlayer = temp;
        }

    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void drawAndEndTurn() throws NoCardsToMoveException, InvalidComboException {
        Player player = turnOrder.remove(0);
        Card drawnCard = player.drawCard();
        currentPlayer.sendMessage("You drew: " + drawnCard);

        AtomicBoolean addPlayer = new AtomicBoolean(true);
        if (drawnCard.getID() == CardFactory.EXPLODING_KITTEN_CARD) {
            addPlayer.set(false);

            player.getHandManager().selectCard(player.getHand().indexOf(drawnCard));

            player.getHand().stream().filter(card -> card.getID() == CardFactory.DEFUSE_CARD).findAny()
                    .ifPresent(defuseCard -> {
                        player.getHandManager().selectCard(player.getHand().indexOf(defuseCard));
                        addPlayer.set(true);
                    });
            player.getHandManager().moveSelectedToStack(false);
            CardInPlayStack.getInstance().resolveTopCard();
        }
        if (turnOrder.size() > 0 && !turnOrder.get(turnOrder.size() - 1).equals(player) && addPlayer.get()) {
            turnOrder.add(player);
        }
        if (turnOrder.size() == 0) {
            System.out.println("Game over!");
            System.exit(0);
        } else {
            currentPlayer = turnOrder.get(0);
        }
    }

    public void endTurnWithoutDraw() {
        Player player = turnOrder.remove(0);
        if (!turnOrder.get(0).equals(player)) {
            turnOrder.add(player);
        }
        currentPlayer = turnOrder.get(0);
    }
    public void makeCurrentPlayerLose() {
        playerManager.removePlayerFromGame(currentPlayer);
        removeAllInstancesOfPlayerFromTurnOrder();
    }

    public void removeAllInstancesOfPlayerFromTurnOrder() {
        List<Player> newTurnOrder = turnOrder.stream().filter(player -> player.playerID != currentPlayer.playerID)
                .collect(Collectors.toList());
        turnOrder.clear();
        turnOrder.addAll(newTurnOrder);
    }

    public List<Player> getTurnOrder() {
        return turnOrder;
    }

    public Player changeActivePlayer(int index) {
        Player player = this.turnOrder.get(index);
        if(getCurrentPlayer().playerID == player.playerID){
            return player;
        }

        List<Player> newOrder = this.turnOrder.stream().filter(p -> p.playerID == player.playerID).collect(Collectors.toList());
        this.turnOrder.removeAll(newOrder);
        newOrder.addAll(this.turnOrder);
        this.turnOrder.clear();
        this.turnOrder.addAll(newOrder);
        this.currentPlayer = turnOrder.get(0);
        return player;
    }

    public long getTurnsLeftToPlayCurrentPlayer() {
        return IntStream.range(0, turnOrder.size())
                .filter((int index) -> turnOrder.get(index).playerID != currentPlayer.playerID)
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException("Card not present in the player's playerHand"));
    }
}