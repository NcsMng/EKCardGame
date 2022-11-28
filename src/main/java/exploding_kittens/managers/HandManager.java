package exploding_kittens.managers;

import exploding_kittens.exceptions.CardNotFoundException;
import exploding_kittens.exceptions.IncorrectNumberOfCardsException;
import exploding_kittens.exceptions.InvalidComboException;
import exploding_kittens.exceptions.NoCardsToMoveException;
import exploding_kittens.model.cards.combos.ThreeCardCombo;
import exploding_kittens.model.cards.combos.TwoCardCombo;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.cards.Card;
import exploding_kittens.model.decks.MainDeck;

import java.util.ArrayList;
import java.util.List;

public class HandManager {

    private List<Card> hand;
    private List<Card> selectedCards;
    private MainDeck mainDeck;
    private CardInPlayStack cardInPlayStack;

    public HandManager() {
        hand = new ArrayList<>();
        selectedCards = new ArrayList<>();
        mainDeck = MainDeck.getInstance();
        cardInPlayStack = CardInPlayStack.getInstance();
    }
    public Card draw() {
        Card drawnCard = mainDeck.draw();
        hand.add(drawnCard);
        return drawnCard;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void selectCard(int i) throws IncorrectNumberOfCardsException {

        Card toMove;
        try {
            toMove = this.hand.remove(i);
        } catch (Exception e) {
            throw new IncorrectNumberOfCardsException("The card index given is invalid!");
        }

        this.selectedCards.add(toMove);
    }


    public void moveSelectedToStack(boolean isSpecialCombo) throws NoCardsToMoveException, InvalidComboException {
        if (this.selectedCards.size() == 0) {
            throw new NoCardsToMoveException("No selected cards available to be moved to stack");
        }

        if (isSpecialCombo) {
            if (TwoCardCombo.isTwoCardCombo(selectedCards)) {
                this.cardInPlayStack.moveCardsToStack(this.makeCombo(2));
            } else if (ThreeCardCombo.isThreeCardCombo(selectedCards)) {
                this.cardInPlayStack.moveCardsToStack(this.makeCombo(3));
            } else {
                throw new InvalidComboException("Not enough cards to make a combo");
            }
        } else {
            this.cardInPlayStack.moveCardsToStack(this.selectedCards);
        }
        this.selectedCards.clear();
    }

    private List<Card> makeCombo(int comboSize) throws InvalidComboException {
        ArrayList<Card> toSendToStack = new ArrayList<>();

        if (comboSize == 2) {
            toSendToStack.add(new TwoCardCombo(selectedCards));
        } else if (comboSize == 3) {
            toSendToStack.add(new ThreeCardCombo(selectedCards));
        }
        return toSendToStack;
    }

    public List<Integer> removeCardAndReturnIndexes(int numberOfIndexesToFind, Class<? extends Card> cardClass) {
        List<Card> playerHand = getHand();
        List<Card> copiedPlayerHand = new ArrayList<>(playerHand);
        List<Integer> indexesToReturn = new ArrayList<>();
        //Classic for cicle to avoid concurrent modification on the list
        for (int i = 0; i < numberOfIndexesToFind; i++) {
            indexesToReturn.add(copiedPlayerHand.stream()
                    .filter(card -> card.getClass() == cardClass)
                    .findFirst()
                    .map(card -> {
                        int indexToReturn = copiedPlayerHand.lastIndexOf(card);
                        copiedPlayerHand.remove(indexToReturn);
                        return indexToReturn;
                    })
                    .orElseThrow(() -> new CardNotFoundException("Card not present!")));
        }
        return indexesToReturn;
    }

    public void addDefuseCard() {
        this.hand.add(new CardFactory().createCard(CardFactory.DEFUSE_CARD));
    }


}
