package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
