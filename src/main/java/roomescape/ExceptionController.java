package roomescape;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(SecurityException.class)
    protected ResponseEntity<String> handleSecurityException(SecurityException exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<String> handleNoSuchElementException(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<String> handleIllegalException(IllegalArgumentException exception) {
        exception.printStackTrace();
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

}
