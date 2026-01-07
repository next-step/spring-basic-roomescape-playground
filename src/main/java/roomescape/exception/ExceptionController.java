package roomescape.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Void> handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(403).build();
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Void> handleConflict(ConflictException e) {
        return ResponseEntity.status(409).build();
    }
}
