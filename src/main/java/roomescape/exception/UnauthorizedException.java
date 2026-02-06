package roomescape.exception;

import lombok.Getter;

@Getter
public class UnauthorizedException extends RuntimeException {
    private final FailMessage failMessage;

    public UnauthorizedException(FailMessage failMessage) {
        super(failMessage.getMessage());
        this.failMessage = failMessage;
    }
}