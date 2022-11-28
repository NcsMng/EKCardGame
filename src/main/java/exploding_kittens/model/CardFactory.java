package exploding_kittens.model;

import exploding_kittens.exceptions.CardLimitReachedException;
import exploding_kittens.exceptions.CardNotFoundException;
import exploding_kittens.model.cards.*;
import exploding_kittens.model.cards.noeffect.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CardFactory {

    public static Map<String, Class<? extends Card>> cardsNameMap = new HashMap<>() {
        {
            put("NopeCard", NopeCard.class);
            put("DefuseCard", DefuseCard.class);
            put("AttackCard", AttackCard.class);
            put("SkipCard", SkipCard.class);
            put("ExplodingKittenCard", ExplodingKittenCard.class);
            put("ShuffleCard", ShuffleCard.class);
            put("SeeTheFutureCard", SeeTheFutureCard.class);
            put("FavorCard", FavorCard.class);
            put("CattermelonCard", CattermelonCard.class);
            put("TacoCatCard", TacoCatCard.class);
            put("HairyPotatoCatCard", HairyPotatoCatCard.class);
            put("RainbowRalphingCatCard", RainbowRalphingCatCard.class);
            put("OverweightBikiniCatCard", OverweightBikiniCatCard.class);
        }
    };

    public static Map<Integer, Class<? extends Card>> cardsIdMap = new HashMap<>() {
        {
            put(1, NopeCard.class);
            put(2, DefuseCard.class);
            put(3, AttackCard.class);
            put(4, SkipCard.class);
            put(5, ExplodingKittenCard.class);
            put(6, ShuffleCard.class);
            put(7, SeeTheFutureCard.class);
            put(8, FavorCard.class);
            put(9, CattermelonCard.class);
            put(10, HairyPotatoCatCard.class);
            put(11, OverweightBikiniCatCard.class);
            put(12, RainbowRalphingCatCard.class);
            put(13, TacoCatCard.class);
        }
    };
    public static final int NOPE_CARD = 1;
    public static final int DEFUSE_CARD = 2;
    public static final int ATTACK_CARD = 3;
    public static final int SKIP_CARD = 4;
    public static final int EXPLODING_KITTEN_CARD = 5;
    public static final int SHUFFLE_CARD = 6;
    public static final int SEE_THE_FUTURE_CARD = 7;
    public static final int FAVOR_CARD = 8;
    public static final int CATTERMELON_CARD = 9;
    public static final int HAIRYPOTATO_CAT_CARD = 10;
    public static final int OVERWEIGHT_BIKINI_CAT_CARD = 11;
    public static final int RAINBOW_RALPHING_CAT_CARD = 12;
    public static final int TACO_CAT_CARD = 13;

    public static int attackCardCount;
    public static int defuseCardCount;
    public static int explodingKittenCardCount;
    public static int favorCardCount;
    public static int nopeCardCount;
    public static int seeTheFutureCardCount;
    public static int shuffleCardCount;
    public static int skipCardCount;

    public static int catterMelonCardCount;

    public static int hairyPotatoCardCount;

    public static int overweightBikiniCatCount;

    public static int rainbowRalphingCatCount;

    public static int tacoCatCardCount;
    public static final int attackCardMax = 4;
    public static final int favorCardMax = 4;
    public static final int nopeCardMax = 5;
    public static final int seeTheFutureCardMax = 5;
    public static final int shuffleCardMax = 4;
    public static final int skipCardMax = 4;
    public static final int catterMelonCardMax = 4;

    public static final int hairyPotatoCardMax = 4;

    public static final int overweightBikiniCatCardMax = 4;

    public static final int rainbowRalphingCatCardMax = 4;

    public static final int tacoCatCardMax = 4;

    public Card createCard(int cardID) {
        Card card;
        switch (cardID) {
            case NOPE_CARD -> {
                if (nopeCardCount < nopeCardMax) {
                    card = new NopeCard();
                    nopeCardCount += 1;
                } else {
                    throw new CardLimitReachedException("NOPE CARD limit reached");
                }
            }
            case DEFUSE_CARD -> {
                card = new DefuseCard();
                defuseCardCount += 1;
            }
            case ATTACK_CARD -> {
                if (attackCardCount < attackCardMax) {
                    card = new AttackCard();
                    attackCardCount += 1;
                } else {
                    throw new CardLimitReachedException("ATTACK CARD limit reached");
                }
            }
            case SKIP_CARD -> {
                if (skipCardCount < skipCardMax) {
                    card = new SkipCard();
                    skipCardCount += 1;
                } else {
                    throw new CardLimitReachedException("SKIP CARD limit reached");
                }
            }
            case EXPLODING_KITTEN_CARD -> {
                card = new ExplodingKittenCard();
                explodingKittenCardCount += 1;
            }
            case SHUFFLE_CARD -> {
                if (shuffleCardCount < shuffleCardMax) {
                    card = new ShuffleCard();
                    shuffleCardCount += 1;
                } else {
                    throw new CardLimitReachedException("SHUFFLE CARD limit reached");
                }
            }
            case SEE_THE_FUTURE_CARD -> {
                if (seeTheFutureCardCount < seeTheFutureCardMax) {
                    card = new SeeTheFutureCard();
                    seeTheFutureCardCount += 1;
                } else {
                    throw new CardLimitReachedException("SEE THE FUTURE CARD limit reached");
                }
            }
            case FAVOR_CARD -> {
                if (favorCardCount < favorCardMax) {
                    card = new FavorCard();
                    favorCardCount += 1;
                } else {
                    throw new CardLimitReachedException("FAVOR CARD limit reached");
                }
            }
            case TACO_CAT_CARD -> {
                if (tacoCatCardCount < tacoCatCardMax) {
                    card = new TacoCatCard();
                    tacoCatCardCount += 1;
                } else {
                    throw new CardLimitReachedException("TACO CAT CARD limit reached");
                }
            }
            case RAINBOW_RALPHING_CAT_CARD -> {
                if (rainbowRalphingCatCount < rainbowRalphingCatCardMax) {
                    card = new RainbowRalphingCatCard();
                    rainbowRalphingCatCount += 1;
                } else {
                    throw new CardLimitReachedException("RAINBOW RALPHING CAT CARD limit reached");
                }
            }
            case OVERWEIGHT_BIKINI_CAT_CARD -> {
                if (overweightBikiniCatCount < overweightBikiniCatCardMax) {
                    card = new OverweightBikiniCatCard();
                    overweightBikiniCatCount += 1;
                } else {
                    throw new CardLimitReachedException("OVERWEIGHT BIKINI CAT CARD limit reached");
                }
            }
            case HAIRYPOTATO_CAT_CARD -> {
                if (hairyPotatoCardCount < hairyPotatoCardMax) {
                    card = new HairyPotatoCatCard();
                    hairyPotatoCardCount += 1;
                } else {
                    throw new CardLimitReachedException("HAIRY POTATO CAT CARD limit reached");
                }
            }
            case CATTERMELON_CARD -> {
                if (catterMelonCardCount < catterMelonCardMax) {
                    card = new CattermelonCard();
                    catterMelonCardCount += 1;
                } else {
                    throw new CardLimitReachedException("CATTERMELON CARD limit reached");
                }
            }
            default -> throw new CardNotFoundException("CardID incorrect for create card in Card Factory");
        }
        return card;
    }


    public static Class<? extends Card> getCardByClassName(String className, Player currentPlayer) {
        Optional<Class<? extends Card>> classOptional = Optional.ofNullable(cardsNameMap.get(className));
        while (classOptional.isEmpty()) {
            currentPlayer.sendMessage("CARDNOTFOUND: " + className + ". Try again");
            className = currentPlayer.readMessage();
            classOptional = Optional.ofNullable(cardsNameMap.get(className));
        }
        return classOptional.get();
    }
}