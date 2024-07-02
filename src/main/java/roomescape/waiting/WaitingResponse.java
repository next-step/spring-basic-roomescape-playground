package roomescape.waiting;

import lombok.Getter;

@Getter
public class WaitingResponse {
    private Long id;
    private Long themeId;
    private String date;
    private String time;
    private int waitingCount;

    public WaitingResponse(Long id, Long themeId, String date, String time, int waitingCount) {
        this.id = id;
        this.themeId = themeId;
        this.date = date;
        this.time = time;
        this.waitingCount = waitingCount;
    }

    public WaitingResponse() {
    }
}
