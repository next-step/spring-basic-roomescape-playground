package roomescape.waiting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WaitingResponse {

    private final Long waitingNumber;

    @JsonCreator
    public WaitingResponse(@JsonProperty("waitingNumber") Long waitingNumber) {
        this.waitingNumber = waitingNumber;
    }

    public Long getWaitingNumber() {
        return waitingNumber;
    }
}
