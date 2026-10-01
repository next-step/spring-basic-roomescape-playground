package roomescape;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.login.LoginAuthenticationException;
import roomescape.reservation.DuplicateReservationException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(LoginAuthenticationException.class)
    public ResponseEntity<Void> handleAuthenticationException(
            LoginAuthenticationException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNoSuchElementException(
            NoSuchElementException e
    ) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler(DuplicateReservationException.class)
    public ResponseEntity<Void> handleDuplicateReservationException(
            DuplicateReservationException e
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .build();
    }
}
