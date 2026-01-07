package roomescape.waiting;

import lombok.Getter;

@Getter
public class WaitingResponse {
    private Long id;

    public WaitingResponse() {
    }

    public WaitingResponse(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}


