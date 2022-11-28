package exploding_kittens.model.decks;

import exploding_kittens.model.cards.Card;

import java.util.ArrayList;
import java.util.List;
public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public boolean addCard(Card card, int i) {
        try {
            this.cards.add(i, card);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

    }

    public boolean addAll(List<Card> cardsToAdd) {
        return this.cards.addAll(cardsToAdd);
    }

    public boolean removeCard(Card card) {
        return this.cards.remove(card);
    }
}