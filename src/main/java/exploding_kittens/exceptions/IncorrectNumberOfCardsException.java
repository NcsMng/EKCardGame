package exploding_kittens.exceptions;

public class IncorrectNumberOfCardsException extends RuntimeException {
    public IncorrectNumberOfCardsException(String message) {
        super(message);
    }
}
