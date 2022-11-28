package exploding_kittens.managers;

import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NopeManager {

    PlayerManager playerManager;
    CardInPlayStack cardInPlayStack;
    static NopeManager nopeManager;
    int secondsToInterruptWithNope = 5;

    public static NopeManager getInstance() {
        if (nopeManager == null) {
            nopeManager = new NopeManager();
        }
        return nopeManager;
    }

    private NopeManager(){
        playerManager = PlayerManager.getInstance();
        cardInPlayStack = CardInPlayStack.getInstance();
    }
    public void nopeChecker(Player currentPlayer, String card) throws InterruptedException {
        playerManager = PlayerManager.getInstance();
        cardInPlayStack = CardInPlayStack.getInstance();
        //After an interruptable card is played everyone has 5 seconds to play Nope
        List<Player> players = playerManager.getPlayers();
        ExecutorService threadpool = Executors.newFixedThreadPool(players.size());
        for (Player p : players) {
            p.sendMessage("Action: Player " + currentPlayer.playerID + " played " + card);
            p.getHand()
                    .stream()
                    .filter(handCard -> handCard.getID() == CardFactory.NOPE_CARD)
                    .findAny()
                    .ifPresent(nopeCard -> {
                        p.sendMessage("Press <Enter> to play Nope");
                        Runnable task = () -> {
                            try {
                                String nextMessage = p.readMessage();

                                if (!nextMessage.equals(" ")) {
                                    p.getHandManager().selectCard(p.getHand().indexOf(nopeCard));
                                    p.getHandManager().moveSelectedToStack(false);
                                    for (Player notify : players)
                                        notify.sendMessage("Player " + p.playerID + " played Nope");
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        };
                        threadpool.execute(task);
                    });
        }
        threadpool.awaitTermination((secondsToInterruptWithNope * 1000L) + 500, TimeUnit.MILLISECONDS);

        for (Player notify : players)
            notify.sendMessage("The timewindow to play Nope passed");

        for (Player notify : players)
            notify.sendMessage("Play another Nope? (alternate between Nope and Yup) \n");
    }
}
