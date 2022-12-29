package by.grigoryev.linkshortener.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class LinkExceptionHandler {

    private final IncorrectData data = new IncorrectData();

    @ExceptionHandler(LinkDoesNotExistException.class)
    public ResponseEntity<IncorrectData> linkDoesNotExistException(LinkDoesNotExistException exception) {
        return createInfoOfIncorrectData(exception.getMessage());
    }

    @ExceptionHandler(PageFormatException.class)
    public ResponseEntity<IncorrectData> pageFormatException(PageFormatException exception) {
        return createInfoOfIncorrectData(exception.getMessage());
    }

    private ResponseEntity<IncorrectData> createInfoOfIncorrectData(String exception) {
        data.setInfo(exception);
        log.error(data.getInfo());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
    }

}
