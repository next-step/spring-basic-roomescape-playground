package roomescape.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CreateMemberFailException extends IllegalArgumentException {
    public CreateMemberFailException(String message) {
        super(message);
    }
}
