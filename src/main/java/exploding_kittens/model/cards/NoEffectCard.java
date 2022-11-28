package exploding_kittens.model.cards;

import exploding_kittens.model.Player;

public class NoEffectCard extends Card {

    public NoEffectCard(int cardID){
        this.cardID = cardID;
    }
    @Override
    public void cardEffect(Player p1, Player p2) {
    }
    @Override
    public String toString() {
        return super.toString();
    }
}
