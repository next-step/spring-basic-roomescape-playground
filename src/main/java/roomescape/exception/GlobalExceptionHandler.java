package roomescape.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.dto.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException occurred:", e);

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(Exception e) {
        log.error("Exception occurred:", e);

        return new ResponseEntity<>(new ErrorResponse("내부 서버 오류가 발생했습니다."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
