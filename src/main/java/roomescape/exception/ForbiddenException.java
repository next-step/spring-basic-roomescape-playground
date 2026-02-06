package roomescape.exception;

import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private final FailMessage failMessage;

    public ForbiddenException(FailMessage failMessage) {
        super(failMessage.getMessage());
        this.failMessage = failMessage;
    }
}