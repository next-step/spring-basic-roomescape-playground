package roomescape.waiting.dto;

import lombok.Getter;
import roomescape.waiting.Waiting;

public class WaitingResponse {

    @Getter
    private Long id;

    private WaitingResponse() {}

    public WaitingResponse(Long id) {
        this.id = id;
    }

    public static WaitingResponse from(Waiting waiting) {
        return new WaitingResponse(waiting.getId());
    }
}
