package roomescape.auth.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
