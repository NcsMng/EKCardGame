package exploding_kittens.exceptions;

public class CardLimitReachedException extends RuntimeException {
    public CardLimitReachedException(String message) {
        super(message);
    }

}
