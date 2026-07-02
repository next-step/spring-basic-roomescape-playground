package roomescape.reservation;

public class WaitingResponse {
    private Long id;
    private int waitingNumber;
    private String theme;
    private String date;
    private String time;

    public WaitingResponse(Long id, int waitingNumber, String theme, String date, String time) {
        this.id = id;
        this.waitingNumber = waitingNumber;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public int getWaitingNumber() {
        return waitingNumber;
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
}
