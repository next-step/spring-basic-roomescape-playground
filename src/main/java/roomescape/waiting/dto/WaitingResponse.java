package roomescape.waiting.dto;

public class WaitingResponse {

    private Long id;
    private String theme;
    private String time;
    private String date;

    private int waitingNumber;

    public WaitingResponse(Long id, String theme, String time, String date, int waitingNumber) {
        this.id = id;
        this.theme = theme;
        this.time = time;
        this.date = date;
        this.waitingNumber = waitingNumber;
    }

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }
}
