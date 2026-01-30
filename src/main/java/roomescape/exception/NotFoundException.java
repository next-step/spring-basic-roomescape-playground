package roomescape.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final FailMessage failMessage;

    public NotFoundException(FailMessage failMessage) {
        super(failMessage.getMessage());
        this.failMessage = failMessage;
    }
}