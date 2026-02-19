package excepciones;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final int httpStatusCode299 = 299;

    @ExceptionHandler(PeajeException.class)
    public ResponseEntity<String> handlePeajeException(PeajeException e) {
        return ResponseEntity.status(httpStatusCode299).body(e.getMessage());
    }

}