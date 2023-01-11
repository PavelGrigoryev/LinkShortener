package by.grigoryev.linkshortener.exception;

public class UserEmailNotFoundException extends RuntimeException {

    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
