package roomescape.member.exception;

import org.springframework.http.HttpStatus;
import roomescape.exception.BusinessException;

public class NoSuchMemberException extends BusinessException {
    public NoSuchMemberException() {
        super("No such member.", HttpStatus.NOT_FOUND);
    }
}
