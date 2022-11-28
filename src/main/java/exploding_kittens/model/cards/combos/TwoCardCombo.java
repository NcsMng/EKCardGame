package exploding_kittens.model.cards.combos;

import exploding_kittens.model.Player;
import exploding_kittens.model.cards.Card;
import exploding_kittens.model.decks.DiscardDeck;

import java.util.List;
import java.util.Random;

public class TwoCardCombo extends Card {


    public static final int COMBO_SIZE = 2;
    private final List<Card> cards;

    public TwoCardCombo(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void cardEffect(Player active, Player target) {
        DiscardDeck discardDeck = DiscardDeck.getInstance();


        List<Card> targetHand = target.getHand();
        Random rnd = new Random();
        int randomIndex = rnd.nextInt(targetHand.size());
        Card targetCard = targetHand.remove(randomIndex);
        target.sendMessage("TWOCARDCOMBO: Player: " + active.playerID + " has stolen " + targetCard + " from your hand by playing a double special combo");
        active.getHand().add(targetCard);
        active.sendMessage("You received " + targetCard + " from player " + target.playerID);
        discardDeck.addAll(cards);
    }

    public static boolean isTwoCardCombo(List<Card> cards) {
        return cards != null && cards.size() == COMBO_SIZE;
    }

    public List<Card> getCards() {
        return cards;
    }
}