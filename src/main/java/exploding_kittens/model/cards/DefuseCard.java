package exploding_kittens.model.cards;

import exploding_kittens.model.decks.DiscardDeck;
import exploding_kittens.model.decks.MainDeck;
import exploding_kittens.managers.CardInPlayStack;
import exploding_kittens.managers.TurnManager;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;

import java.util.List;
import java.util.Stack;

public class DefuseCard extends Card {

    public DefuseCard() {
        this.cardID = 2;
    }

    @Override
    public void cardEffect(Player p1, Player p2) { //If there's an Exploding Kitten card into the stack at position 0 we deplete our defuse card to cancel its effect.
        CardInPlayStack stack = CardInPlayStack.getInstance();
        TurnManager turnManager = TurnManager.getInstance();
        MainDeck mainDeck = MainDeck.getInstance();
        DiscardDeck discardDeck = DiscardDeck.getInstance();
        Player currentPlayer = turnManager.getCurrentPlayer();
        List<Player> players = turnManager.getTurnOrder();

        //If cardAction is used, that means we poped the Defuse card from the stack thus leaving the exploding kitten in the stack
        if (stack.getStack().isEmpty()) {
            turnManager.getCurrentPlayer().getHand().add(this);
            return;
        }

        if (stack.getStack().elementAt(0).getID() == CardFactory.EXPLODING_KITTEN_CARD) {
            Card explodingKitten = stack.getStack().elementAt(0);
            stack.setStack(new Stack<>());
            if(mainDeck.getCardCount() - 1 != -1){
                currentPlayer.sendMessage("EXPLODINGKITTEN: You defused the kitten. Where in the deck do you wish to place the ExplodingKitten? [0.." + (mainDeck.getCardCount() - 1) + "]");
                mainDeck.insertCard(explodingKitten, Integer.parseInt(currentPlayer.readMessage()));
            }else {
                currentPlayer.sendMessage("EXPLODINGKITTEN: You defused the kitten. Card will be inserted into the deck");
                mainDeck.insertCard(explodingKitten, 0);
            }
            discardDeck.addCard(this);
            players.forEach(player -> player.sendMessage("Player " + currentPlayer.playerID + " successfully defused a kitten"));
        } else {
            turnManager.getCurrentPlayer().getHand().add(this);
        }
    }
    @Override
    public String toString(){
        return super.toString();
    }
}