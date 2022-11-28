package exploding_kittens.model.cards;

import exploding_kittens.managers.TurnManager;
import exploding_kittens.model.Player;
import exploding_kittens.model.decks.DiscardDeck;
import exploding_kittens.model.decks.MainDeck;

import java.util.ArrayList;
import java.util.List;

public class SeeTheFutureCard extends Card {



    public SeeTheFutureCard() {
        this.cardID = 7;
    }

    @Override
    public void cardEffect(Player p1, Player p2) {
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        TurnManager turnManager = TurnManager.getInstance();

        int numberOfCardsToUnveil = 3;
        List<Card> cardsToReveal = new ArrayList<>();

        MainDeck mainDeck = MainDeck.getInstance();
        if (mainDeck.getCardCount() < numberOfCardsToUnveil) {
            numberOfCardsToUnveil = mainDeck.getCardCount();
        }
        for (int i = 0; i < numberOfCardsToUnveil; i++) {
            cardsToReveal.add(mainDeck.getCards().get(i));
        }
        String cardRevealed = cardsToReveal.stream()
                .map(Card::toString)
                .reduce(" ", (s, s2) -> s + " " + s2);

        turnManager.getCurrentPlayer().sendMessage(("The top 3 cards are: " + cardRevealed));
        discardDeck.addCard(this);
    }
    @Override
    public String toString() {
        return super.toString();
    }
}