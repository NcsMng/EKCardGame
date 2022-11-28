package exploding_kittens.model.cards;

import exploding_kittens.managers.TurnManager;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;
import exploding_kittens.model.decks.DiscardDeck;

import java.util.List;

public class AttackCard extends Card {
    public AttackCard() {
        this.cardID = 3;
    }


    @Override
    public void cardEffect(Player p1, Player p2) {

        DiscardDeck discardDeck = DiscardDeck.getInstance();
        TurnManager turnManager = TurnManager.getInstance();
        List<Player> turnOrder = turnManager.getTurnOrder();

        if (!discardDeck.getCards().isEmpty() && discardDeck.getCards().get(0).cardID == CardFactory.ATTACK_CARD) {
            long numberOfTurnsAccumulated = turnManager.getTurnsLeftToPlayCurrentPlayer();
            turnManager.removeAllInstancesOfPlayerFromTurnOrder();
            for (int i = 0; i < numberOfTurnsAccumulated; i++) {
                turnOrder.add(0, p2);
            }
            turnOrder.add(0, p2);
        } else {
            turnManager.removeAllInstancesOfPlayerFromTurnOrder();
            turnOrder.add(0, p2);
        }
        turnOrder.add(turnOrder.size(), p1);
        turnManager.setCurrentPlayer(turnManager.getTurnOrder().get(0));
        discardDeck.addCard(this);
    }

    @Override
    public String toString(){
        return super.toString();
    }
}