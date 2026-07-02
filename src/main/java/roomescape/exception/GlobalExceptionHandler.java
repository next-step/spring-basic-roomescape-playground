package roomescape.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import roomescape.auth.exception.ExpiredTokenException;
import roomescape.auth.exception.InvalidTokenException;
import roomescape.member.exception.NoSuchMemberException;

import java.time.LocalDateTime;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ErrorResponseBody> handleExpiredTokenException(ExpiredTokenException e) {
        return handleBusinessException(e);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseBody> handleInvalidTokenException(InvalidTokenException e) {
        return handleBusinessException(e);
    }

    @ExceptionHandler(NoSuchMemberException.class)
    public ResponseEntity<ErrorResponseBody> handleNoSuchMemberException(NoSuchMemberException e) {
        return handleBusinessException(e);
    }

    private ResponseEntity<ErrorResponseBody> handleBusinessException(BusinessException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponseBody(e.getMessage(), LocalDateTime.now()));
    }
}
