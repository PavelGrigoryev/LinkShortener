package by.grigoryev.linkshortener.exception;

public class LinkDoesNotExistException extends RuntimeException {

    public LinkDoesNotExistException(String message) {
        super(message);
    }

}
