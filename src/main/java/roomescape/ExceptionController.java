package roomescape;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.auth.ForbiddenException;
import roomescape.auth.UnauthorizedException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Void> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Void> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(403).build();
    }
}