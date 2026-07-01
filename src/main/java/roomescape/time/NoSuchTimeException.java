package roomescape.time;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class NoSuchTimeException extends BusinessException {
    public NoSuchTimeException() {
        super("No such Time", HttpStatus.NOT_FOUND);
    }
}
