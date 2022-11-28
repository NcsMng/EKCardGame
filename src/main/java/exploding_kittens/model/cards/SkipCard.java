package exploding_kittens.model.cards;

import exploding_kittens.managers.TurnManager;
import exploding_kittens.model.Player;
import exploding_kittens.model.decks.DiscardDeck;

public class SkipCard extends Card {
    public SkipCard() {
        this.cardID = 4;
    }


    @Override
    public void cardEffect(Player p1, Player p2) {
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        TurnManager.getInstance().endTurnWithoutDraw();
        discardDeck.addCard(this);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}