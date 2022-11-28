package exploding_kittens.model.cards;

import exploding_kittens.model.Player;
import org.apache.commons.lang3.StringUtils;

public abstract class Card {
    protected int cardID;

    public int getID() {
        return this.cardID;
    }

    @Override
    public String toString() {
        String fullClassPath = this.getClass().getName();
        return StringUtils.substringAfterLast(fullClassPath, '.');
    }

    abstract public void cardEffect(Player p1, Player p2);


}
