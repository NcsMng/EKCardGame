package exploding_kittens.managers;

import exploding_kittens.exceptions.InvalidNopeTargetException;
import exploding_kittens.model.Player;
import exploding_kittens.model.cards.Card;

import java.util.List;
import java.util.Stack;
public class CardInPlayStack {

    private static CardInPlayStack cardInPlayStack;

    private Stack<Card> stack;

    private CardInPlayStack() {
        this.stack = new Stack<>();
    }


    public static CardInPlayStack getInstance() {
        if (cardInPlayStack == null) {
            cardInPlayStack = new CardInPlayStack();
        }
        return cardInPlayStack;
    }

    public void addCard(Card card) {
        this.stack.add(card);
    }

    public void resolveTopCard() {
        this.stack.pop().cardEffect(null, null);
    }

    public void resolveTopCard(Player player1, Player player2) {
        this.stack.pop().cardEffect(player1, player2);
    }

    @SuppressWarnings("unchecked")
    public Stack<Card> getStack() {
        return (Stack<Card>) stack.clone();
    }

    public void setStack(Stack<Card> stack) {
        this.stack = stack;
    }

    public void moveCardsToStack(List<Card> cardsToMove) {
        stack.addAll(cardsToMove);
    }

    public Card counterTopCard() {
        if (stack.isEmpty()) {
            throw new InvalidNopeTargetException("No cards to counter with NOPE");
        }
        return stack.pop();
    }

}