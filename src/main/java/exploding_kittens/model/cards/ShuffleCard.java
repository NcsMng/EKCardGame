package exploding_kittens.model.cards;


import exploding_kittens.model.decks.DiscardDeck;
import exploding_kittens.model.decks.MainDeck;
import exploding_kittens.model.Player;

public class ShuffleCard extends Card{
    public ShuffleCard() {
        this.cardID = 6;
    }

    @Override
    public void cardEffect(Player p1, Player p2) {
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        MainDeck mainDeck = MainDeck.getInstance();
        mainDeck.shuffleDeck();
        discardDeck.addCard(this);
    }
    @Override
    public String toString() {
        return super.toString();
    }

}