package roomescape;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.exception.AuthenticationException;
import roomescape.exception.DuplicateReservationException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Void> handleAuthentication(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<Void> handleDuplicate(DuplicateReservationException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        return ResponseEntity.badRequest().build();
    }
}
