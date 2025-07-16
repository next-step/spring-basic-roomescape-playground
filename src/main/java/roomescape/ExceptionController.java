package roomescape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.auth.exception.UnauthenticatedException;

@ControllerAdvice
public class ExceptionController {

    private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleRuntimeException(Exception e) {
        log.error("예상치 못한 예외가 발생했습니다.", e);
        return ResponseEntity.internalServerError().body("서버 내부 오류가 발생했습니다.");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> handleUnauthenticatedException(UnauthenticatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
