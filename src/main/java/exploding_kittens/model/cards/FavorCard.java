package exploding_kittens.model.cards;

import exploding_kittens.model.decks.DiscardDeck;
import exploding_kittens.managers.HandManager;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;

import java.util.List;

public class FavorCard extends Card {

    public FavorCard() {
        this.cardID = 8;
    }

    @Override
    public void cardEffect(Player p1, Player p2) {
        HandManager hand1 = p1.getHandManager();
        HandManager hand2 = p2.getHandManager();
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        List<Card> targetHand = hand2.getHand();
        if (targetHand.size() > 0) {
            p2.sendMessage("Your hand: " + targetHand);
            p2.sendMessage("FAVOR: Give a card to Player " + p1.playerID);
            Class<? extends Card> selectedCard = CardFactory.getCardByClassName(p2.readMessage(), p2);
            List<Integer> indexesOfCards = hand2.removeCardAndReturnIndexes(1, selectedCard);
            int indexToRemove = indexesOfCards.get(0);
            if (indexToRemove < targetHand.size()) {
                Card card = targetHand.remove(indexToRemove);
                hand1.getHand().add(card);
                p1.sendMessage("You got " + card + " from " + p2.playerID);
            } else {
                throw new IndexOutOfBoundsException("Not a valid card selected");
            }
            discardDeck.addCard(this);
        } else {
            p1.sendMessage("Player " + p2.playerID + " has no cards in hand ");
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}