package roomescape.exception;

import lombok.Getter;

@Getter
public class ConflictException extends RuntimeException {
    private final FailMessage failMessage;

    public ConflictException(FailMessage failMessage) {
        super(failMessage.getMessage());
        this.failMessage = failMessage;
    }
}
