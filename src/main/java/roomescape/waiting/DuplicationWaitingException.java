package roomescape.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicationWaitingException extends RuntimeException {
    public DuplicationWaitingException() {
    }

    public DuplicationWaitingException(String message) {
        super(message);
    }
}
