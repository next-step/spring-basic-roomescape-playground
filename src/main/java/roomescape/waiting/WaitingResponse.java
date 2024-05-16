package roomescape.waiting;

public class WaitingResponse {
    private Long id;
    private Long theme;
    private String date;
    private String time;
    private int waitingNumber;

    public WaitingResponse(Long id, Long theme, String date, String time, int waitingNumber) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.waitingNumber = waitingNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTheme() {
        return theme;
    }

    public void setTheme(Long theme) {
        this.theme = theme;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getWaitingNumber() {
        return waitingNumber;
    }

    public void setWaitingNumber(int waitingNumber) {
        this.waitingNumber = waitingNumber;
    }
}
