package roomescape.waiting;

public class WaitingResponse {

    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;
    private Long waitingNumber;

    public WaitingResponse(Long id, String theme, String date, String time, String status, Long waitingNumber) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
        this.waitingNumber = waitingNumber;
    }

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public Long getWaitingNumber() {
        return waitingNumber;
    }
}
