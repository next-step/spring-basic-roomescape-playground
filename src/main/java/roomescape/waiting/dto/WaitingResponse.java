package roomescape.waiting.dto;

public class WaitingResponse {
    private Long waitingId;
    private Long theme;
    private String date;
    private String time;
    private int waitingNumber;

    public WaitingResponse(Long waitingId, Long theme, String date, String time, int waitingNumber) {
        this.waitingId = waitingId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.waitingNumber = waitingNumber;
    }

    public Long getWaitingId() {
        return waitingId;
    }

    public Long getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }
}
