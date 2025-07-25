package roomescape;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.AuthorizationException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> handleAuthorizationException(AuthorizationException e) {
        e.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
