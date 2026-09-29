package roomescape;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Void> handleExpiredJwtException(ExpiredJwtException e) {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Void> handleJwtException(JwtException e) {
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Void> handleResponseStatusException(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).build();
    }
}
