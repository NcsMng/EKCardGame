package exploding_kittens.model.decks;

import exploding_kittens.model.cards.noeffect.*;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainDeck {

    private static MainDeck mainDeck;

    Deck deck;
    public CardFactory factory;

    public static MainDeck getInstance() {
        if (mainDeck == null) {
            mainDeck = new MainDeck();
        }
        return mainDeck;
    }

    public static void tearDown() {
        mainDeck = null;
    }

    private MainDeck() {
        deck = new Deck(new ArrayList<>());
        factory = new CardFactory();
    }

    public int getCardCount() {
        return mainDeck.getCards().size();
    }

    public List<Card> getCards() {
        return mainDeck.deck.getCards();
    }

    public void setCards(List<Card> cards) {
        mainDeck.deck = new Deck(cards);
    }

    public boolean insertCard(Card card, int position) {
        return mainDeck.deck.addCard(card, position);
    }

    public Card draw() {
        Card drawCard = getTopCard();
        mainDeck.deck.removeCard(drawCard);
        return drawCard;
    }

    private Card getTopCard() {
        return mainDeck.getCards().get(0);
    }

    public void shuffleDeck() {
        List<Card> toShuffle = mainDeck.deck.getCards();
        Collections.shuffle(toShuffle);
        mainDeck.deck.setCards(toShuffle);
    }

    public void initStartingDeck() {
        if (mainDeck.deck.getCards().size() == 0) {
            addFavorCards();
            addShuffleCards();
            addSkipCards();
            addAttackCards();
            addNormalCards();
            addNopeCards();
            addSeeTheFutureCards();
        }
        shuffleDeck();
    }

    private void addFavorCards() {
        for (int i = 0; i < CardFactory.favorCardMax; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.FAVOR_CARD), 0);
        }
    }

    private void addShuffleCards() {
        for (int i = 0; i < CardFactory.shuffleCardMax; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.SHUFFLE_CARD), 0);
        }
    }

    private void addSkipCards() {
        for (int i = 0; i < CardFactory.skipCardMax; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.SKIP_CARD), 0);
        }
    }

    private void addAttackCards() {
        for (int i = 0; i < CardFactory.attackCardMax; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.ATTACK_CARD), 0);
        }
    }

    private void addNormalCards() {
        for (int i = 0; i < CardFactory.catterMelonCardMax; i++) {
            CattermelonCard card = (CattermelonCard) factory.createCard(CardFactory.CATTERMELON_CARD);
            mainDeck.insertCard(card, 0);
        }
        for (int i = 0; i < CardFactory.hairyPotatoCardMax; i++) {
            HairyPotatoCatCard card = (HairyPotatoCatCard) factory.createCard(CardFactory.HAIRYPOTATO_CAT_CARD);
            mainDeck.insertCard(card, 0);
        }
        for (int i = 0; i < CardFactory.overweightBikiniCatCardMax; i++) {
            OverweightBikiniCatCard card = (OverweightBikiniCatCard) factory.createCard(CardFactory.OVERWEIGHT_BIKINI_CAT_CARD);
            mainDeck.insertCard(card, 0);
        }
        for (int i = 0; i < CardFactory.tacoCatCardMax; i++) {
            TacoCatCard card = (TacoCatCard) factory.createCard(CardFactory.TACO_CAT_CARD);
            mainDeck.insertCard(card, 0);
        }
        for (int i = 0; i < CardFactory.rainbowRalphingCatCardMax; i++) {
            RainbowRalphingCatCard card = (RainbowRalphingCatCard) factory.createCard(CardFactory.RAINBOW_RALPHING_CAT_CARD);
            mainDeck.insertCard(card, 0);
        }
    }

    private void addNopeCards() {
        for (int i = 0; i < CardFactory.nopeCardMax; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.NOPE_CARD), 0);
        }
    }

    private void addSeeTheFutureCards() {
        for (int i = 0; i < CardFactory.seeTheFutureCardMax; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.SEE_THE_FUTURE_CARD), 0);
        }
    }

    public void populateDeck(int numPlayers) {
        addExplodingKittenCards(numPlayers);
        addDefuseCards(numPlayers);
        shuffleDeck();
    }

    private void addExplodingKittenCards(int numPlayers) {
        for (int i = 0; i < numPlayers - 1; i++) {
            mainDeck.insertCard(factory.createCard(CardFactory.EXPLODING_KITTEN_CARD), 0);
        }
    }

    private void addDefuseCards(int numPlayers) {
        mainDeck.insertCard(factory.createCard(CardFactory.DEFUSE_CARD), 0);
        if (2 <= numPlayers && numPlayers <= 4){
            mainDeck.insertCard(factory.createCard(CardFactory.DEFUSE_CARD), 0);
        }
    }

}