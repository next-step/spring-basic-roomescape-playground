package roomescape.domain.waiting;

public class WaitingRequest {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;
    private Long waitingNumber;

    public WaitingRequest(String name, String theme, String date, String time) {
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
