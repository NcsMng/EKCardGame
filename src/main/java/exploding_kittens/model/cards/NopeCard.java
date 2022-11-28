package exploding_kittens.model.cards;

import exploding_kittens.model.decks.DiscardDeck;
import exploding_kittens.managers.CardInPlayStack;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;

public class NopeCard extends Card {
    public NopeCard() {
        this.cardID = 1;
    }

    @Override
    public void cardEffect(Player p1, Player p2) {
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        CardInPlayStack cardInPlayStack = CardInPlayStack.getInstance();
        Card card = cardInPlayStack.counterTopCard();
        if (card.getID() == CardFactory.NOPE_CARD) {
            cardInPlayStack.resolveTopCard(p1, p2);
        }
        discardDeck.addCard(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}