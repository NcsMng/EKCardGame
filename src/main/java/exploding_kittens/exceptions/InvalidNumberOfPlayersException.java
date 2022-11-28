package exploding_kittens.exceptions;

public class InvalidNumberOfPlayersException extends RuntimeException {

    public InvalidNumberOfPlayersException(String message) {
        super(message);
    }


}