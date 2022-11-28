package exploding_kittens.model.cards.combos;

import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;
import exploding_kittens.model.cards.Card;
import exploding_kittens.model.decks.DiscardDeck;

import java.util.List;
import java.util.stream.IntStream;

public class ThreeCardCombo extends Card {
    public static final int COMBO_SIZE = 3;
    private List<Card> cards;

    public ThreeCardCombo(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public void cardEffect(Player active, Player target) {
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        active.sendMessage("THREECARDCOMBO: Select a card to get from " + target.playerID + " hand");
        String cardName = active.readMessage();
        Class<? extends Card> cardClass = CardFactory.getCardByClassName(cardName, active);
        List<Card> targetHand = target.getHand();

        IntStream.range(0, targetHand.size())
                .filter(index -> targetHand.get(index).getClass() == cardClass)
                .findFirst()
                .ifPresentOrElse(indexOfCardPicked ->
                        {
                            Card picked = targetHand.remove(indexOfCardPicked);
                            target.sendMessage("Player " + active.playerID + " stole " + picked);
                            active.getHand().add(picked);
                            active.sendMessage("You received " + picked + " from player " + target.playerID + "\n");
                        },
                        () -> active.sendMessage("The player did not have any " + cardName));
        discardDeck.addAll(cards);
    }

    public static boolean isThreeCardCombo(List<Card> cards) {
        return cards != null && cards.size() == COMBO_SIZE;
    }


    public List<Card> getCards() {
        return cards;
    }

}