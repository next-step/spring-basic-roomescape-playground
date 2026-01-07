package roomescape.waiting;

public class WaitingResponse {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;
    private String status;

    public WaitingResponse() {
    }

    public WaitingResponse(Long id, String name, String theme, String date, String time, String status) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
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

    public String getStatus() {
        return status;
    }
}
