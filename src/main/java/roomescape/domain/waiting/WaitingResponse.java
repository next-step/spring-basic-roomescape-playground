package roomescape.domain.waiting;

public class WaitingResponse {

    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;
    private Long waitingNumber;

    public WaitingResponse(Long id, String name, String theme, String date, String time, Long waitingNumber) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.waitingNumber = waitingNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public Long getWaitingNumber() {
        return waitingNumber;
    }
}
