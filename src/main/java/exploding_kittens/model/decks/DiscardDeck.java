package exploding_kittens.model.decks;

import exploding_kittens.model.cards.Card;

import java.util.List;
public class DiscardDeck {

    private static DiscardDeck discardDeck;

    Deck deck;

    public static DiscardDeck getInstance() {
        if (discardDeck == null) {
            discardDeck = new DiscardDeck();
        }
        return discardDeck;
    }

    private DiscardDeck() {
        deck = new Deck();
    }

    public static void tearDown() {
        discardDeck = null;
    }

    public List<Card> getCards() {
        return discardDeck.deck.getCards();
    }

    public void addCard(Card card) {
        discardDeck.deck.addCard(card, 0);
    }

    public void addAll(List<Card> cardsToAdd) {
        deck.addAll(cardsToAdd);
    }

}