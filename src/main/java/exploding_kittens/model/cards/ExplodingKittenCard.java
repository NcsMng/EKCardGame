package exploding_kittens.model.cards;


import exploding_kittens.managers.TurnManager;
import exploding_kittens.model.Player;

public class ExplodingKittenCard extends Card{
    public ExplodingKittenCard() {
        this.cardID = 5;

    }
    @Override
    public void cardEffect(Player p1, Player p2) {
        TurnManager turnManager = TurnManager.getInstance();
        turnManager.makeCurrentPlayerLose();
        turnManager.getTurnOrder().forEach(player -> player.sendMessage("Player " + turnManager.getCurrentPlayer().playerID + " exploded"));
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
