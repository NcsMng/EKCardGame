package exploding_kittens.model;

import exploding_kittens.model.cards.Card;
import exploding_kittens.managers.HandManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Player {
    public int playerID;

    private final HandManager handManager;
    public boolean online;
    public boolean isBot;
    public Socket connection;

    public ObjectInputStream inFromClient;
    public ObjectOutputStream outToClient;

    public Player(int playerID, boolean isBot, Socket connection, ObjectInputStream inFromClient, ObjectOutputStream outToClient) throws IOException {
        this.playerID = playerID;
        this.connection = connection;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        this.isBot = isBot;
        this.online = connection != null;
        this.handManager = new HandManager();
    }

    public void sendMessage(Object message) {
        if (online) {
            try {
                outToClient.writeObject(message);
            } catch (Exception ignored) {
            }
        } else if (!isBot) {
            System.out.println(message);
        }
    }

    public String readMessage() {
        String word = " ";
        if (online)
            try {
                System.out.println("PLAYER: " + this.playerID + " EXPLECTED TO WRITE");
                word = (String) inFromClient.readObject();
                System.out.println("PLAYER: " + this.playerID + " WROTE " + word);
            } catch (Exception e) {
                System.out.println("Reading from client failed: " + e.getMessage());
            }
        return word;
    }

    public Card drawCard() {
        return handManager.draw();
    }

    public void addDefuseCardToHand() {
        this.handManager.addDefuseCard();
    }

    public HandManager getHandManager() {
        return handManager;
    }

    public List<Card> getHand() {
        return handManager.getHand();
    }
}
