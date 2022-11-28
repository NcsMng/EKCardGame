package exploding_kittens.managers;

import exploding_kittens.exceptions.CardNotFoundException;
import exploding_kittens.model.CardFactory;
import exploding_kittens.model.Player;
import exploding_kittens.model.cards.Card;

import java.util.List;
import java.util.Optional;

public class CardEffectManager {

    static CardEffectManager cardEffectManager;
    TurnManager turnManager;

    public static CardEffectManager getInstance() {
        if (cardEffectManager == null) {
            cardEffectManager = new CardEffectManager();
        }
        return cardEffectManager;
    }
    private CardEffectManager(){
        turnManager = TurnManager.getInstance();
    }

    public void makeDoubleCardSpecialCombo(int targetId, String cardClassName) throws InterruptedException {
        turnManager = TurnManager.getInstance();
        Player currentPlayer = turnManager.getCurrentPlayer();
        PlayerManager playerManager = PlayerManager.getInstance();

        Player targetPlayer = playerManager.getPlayerByPlayerId(targetId, currentPlayer);
        HandManager handManager = currentPlayer.getHandManager();

        Class<? extends Card> cardClassByName = CardFactory.getCardByClassName(cardClassName, currentPlayer);

        List<Integer> integers = handManager.removeCardAndReturnIndexes(2, cardClassByName);
        integers.forEach(handManager::selectCard);

        handManager.moveSelectedToStack(true);
        NopeManager.getInstance().nopeChecker(currentPlayer, "Two of a kind against player " + targetPlayer.playerID);
        CardInPlayStack.getInstance().resolveTopCard(currentPlayer, targetPlayer);


    }

    public void makeTripleCardSpecialCombo(int targetId, String cardClassName) throws InterruptedException {
        turnManager = TurnManager.getInstance();
        Player currentPlayer = turnManager.getCurrentPlayer();
        PlayerManager playerManager = PlayerManager.getInstance();

        Player targetPlayer = playerManager.getPlayerByPlayerId(targetId, currentPlayer);
        HandManager handManager = currentPlayer.getHandManager();

        Class<? extends Card> cardByClassName = CardFactory.getCardByClassName(cardClassName, currentPlayer);

        handManager.removeCardAndReturnIndexes(3, cardByClassName)
                .forEach(handManager::selectCard);

        handManager.moveSelectedToStack(true);
        NopeManager.getInstance().nopeChecker(currentPlayer, "Three of a kind against activePlayer " + targetPlayer.playerID);
        CardInPlayStack.getInstance().resolveTopCard(currentPlayer, targetPlayer);
    }

    public void useSkipCardEffect() throws InterruptedException {
        turnManager = TurnManager.getInstance();
        Player currentPlayer = turnManager.getCurrentPlayer();
        HandManager handManager = currentPlayer.getHandManager();

        Card skipCardInHand = currentPlayer.getHand().stream().filter(card -> card.getID() == CardFactory.SKIP_CARD).findAny().orElseThrow(() -> new CardNotFoundException("Player has no SKIP card in hand"));
        handManager.selectCard(handManager.getHand().lastIndexOf(skipCardInHand));
        handManager.moveSelectedToStack(false);
        NopeManager.getInstance().nopeChecker(currentPlayer, "Skip");
        CardInPlayStack.getInstance().resolveTopCard(null, null);
    }

    public void useSeeTheFutureCardEffect() throws InterruptedException {
        turnManager = TurnManager.getInstance();
        Player currentPlayer = turnManager.getCurrentPlayer();
        HandManager handManager = currentPlayer.getHandManager();
        Card seeTheFutureCardInHand = currentPlayer.getHand().stream().filter(card -> card.getID() == CardFactory.SEE_THE_FUTURE_CARD).findAny().orElseThrow(() -> new CardNotFoundException("Player has no SEETHEFUTURE card in hand"));
        handManager.selectCard(handManager.getHand().lastIndexOf(seeTheFutureCardInHand));
        handManager.moveSelectedToStack(false);
        NopeManager.getInstance().nopeChecker(currentPlayer, "SeeTheFuture");
        CardInPlayStack.getInstance().resolveTopCard(null, null);
    }

    public void useShuffleCardEffect() throws InterruptedException {
        turnManager = TurnManager.getInstance();

        Player currentPlayer = turnManager.getCurrentPlayer();
        HandManager handManager = currentPlayer.getHandManager();
        Card shuffleCardInHand = currentPlayer.getHand().stream().filter(card -> card.getID() == CardFactory.SHUFFLE_CARD).findAny().orElseThrow(() -> new CardNotFoundException("Player has no SHUFFLE card in hand"));
        handManager.selectCard(handManager.getHand().lastIndexOf(shuffleCardInHand));
        handManager.moveSelectedToStack(false);
        NopeManager.getInstance().nopeChecker(currentPlayer, "Shuffle");
        CardInPlayStack.getInstance().resolveTopCard(null, null);
    }

    public void useFavorCardEffect(Player sourcePlayer, int playerID) throws InterruptedException {
        turnManager = TurnManager.getInstance();
        PlayerManager playerManager = PlayerManager.getInstance();

        Player targetPlayer = playerManager.getPlayerByPlayerId(playerID, sourcePlayer);
        Player currentPlayer = turnManager.getCurrentPlayer();
        HandManager handManager = currentPlayer.getHandManager();
        Card favorCardInHand = currentPlayer.getHand().stream().filter(card -> card.getID() == CardFactory.FAVOR_CARD).findAny().orElseThrow(() -> new CardNotFoundException("Player has no FAVOR card in hand"));
        handManager.selectCard(handManager.getHand().lastIndexOf(favorCardInHand));
        handManager.moveSelectedToStack(false);
        NopeManager.getInstance().nopeChecker(currentPlayer, "Favor player " + targetPlayer.playerID);
        CardInPlayStack.getInstance().resolveTopCard(sourcePlayer, targetPlayer);
    }

    public void useAttackCardEffect() throws Exception {
        turnManager = TurnManager.getInstance();
        PlayerManager playerManager = PlayerManager.getInstance();
        Player currentPlayer = turnManager.getCurrentPlayer();
        HandManager handManager = currentPlayer.getHandManager();
        Card attackCardInHand = currentPlayer.getHand().stream().filter(card -> card.getID() == CardFactory.ATTACK_CARD).findAny().orElseThrow(() -> new CardNotFoundException("Player has no ATTACK card in hand"));
        handManager.selectCard(handManager.getHand().lastIndexOf(attackCardInHand));
        handManager.moveSelectedToStack(false);

        Optional<Player> nextPlayer = turnManager.getTurnOrder().stream()
                .filter(player -> player.playerID != currentPlayer.playerID)
                .findFirst();
        if (nextPlayer.isPresent()) {
            NopeManager.getInstance().nopeChecker(currentPlayer, "Attack player " + nextPlayer.get().playerID);
            CardInPlayStack.getInstance().resolveTopCard(currentPlayer, nextPlayer.get());
        } else {
            for (Player player : playerManager.getPlayers()) {
                player.sendMessage("Player " + currentPlayer.playerID + " has won the game");
            }
            System.exit(0);
        }

    }
}
