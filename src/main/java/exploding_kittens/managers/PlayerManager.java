package exploding_kittens.managers;


import exploding_kittens.exceptions.InvalidNumberOfPlayersException;
import exploding_kittens.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayerManager {

    private static final int INITIAL_HAND_NUMBER_OF_CARDS_TO_DRAW = 7;
    public List<Player> players;
    private static PlayerManager playerManager;

    private PlayerManager() {
        players = new ArrayList<>();
    }

    public static PlayerManager getInstance() {
        if (playerManager == null) {
            playerManager = new PlayerManager();
        }
        return playerManager;
    }

    public void addPlayers(int numPlayers, int numBots, ServerSocket serverSocket) throws InvalidNumberOfPlayersException, IOException, ClassNotFoundException, InterruptedException {
        if (numPlayers + numBots < 2 || numPlayers + numBots > 5) {
            throw new InvalidNumberOfPlayersException("Players should be atleast 2 and max 5");
        }
        for (int i = 0; i < numBots; i++) {
            players.add(new Player(i + 1, true, null, null, null)); //add a bot
        }
        for (int i = numBots; i < numPlayers + numBots; i++) {
            Socket connectionSocket = serverSocket.accept();
            ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
            ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
            players.add(new Player(i, false, connectionSocket, inFromClient, outToClient));
            outToClient.writeObject("CONNECTED TO GAME SERVER!");
            Thread.sleep(100);
            System.out.println(inFromClient.readObject());
        }
    }

    public void makePlayerDrawInitialHand() {

        for (Player player : players) {
            for (int j = 0; j < INITIAL_HAND_NUMBER_OF_CARDS_TO_DRAW; j++) {
                player.drawCard();
            }
            player.addDefuseCardToHand();
        }
    }


    public List<Player> getPlayers() {
        return this.players;
    }

    public List<Integer> getAllPlayersIDs() {
        return this.getPlayers().stream().map(player -> player.playerID).collect(Collectors.toList());
    }

    public Optional<Player> getPlayerOptionalByPlayerId(Integer playerId) {
        return this.getPlayers().stream().filter(player -> player.playerID == playerId)
                .findFirst();
    }

    public Player getPlayerByPlayerId(int targetId, Player currentPlayer) {
        Optional<Player> playerOptional = getPlayerOptionalByPlayerId(targetId);
        while (playerOptional.isEmpty() || playerOptional.get().playerID == currentPlayer.playerID) {
            if (playerOptional.isPresent() && playerOptional.get().playerID == currentPlayer.playerID) {
                currentPlayer.sendMessage("PLAYERNOTFOUND: " + targetId + ". Cannot target self! Try again.");
            } else {
                currentPlayer.sendMessage("PLAYERNOTFOUND: " + targetId + ". Try again");
            }
            targetId = Integer.parseInt(currentPlayer.readMessage());
            playerOptional = playerManager.getPlayerOptionalByPlayerId(targetId);
        }
        return playerOptional.get();
    }

    public void removePlayerFromGame(Player player) {
        players.remove(player);
    }
}