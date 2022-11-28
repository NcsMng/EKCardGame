package exploding_kittens.exceptions;

public class NoCardsToMoveException extends RuntimeException{

    public NoCardsToMoveException(String message) {
        super(message);
    }

}
