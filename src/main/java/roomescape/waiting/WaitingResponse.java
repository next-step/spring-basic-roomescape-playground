package roomescape.waiting;

public class WaitingResponse {
    private Long id;
    private String name;
    private String date;
    private String themeName;
    private String timeValue;
    private Long waitingRank;

    public WaitingResponse(Long id, String name, String date, String themeName, String timeValue, Long waitingRank) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.themeName = themeName;
        this.timeValue = timeValue;
        this.waitingRank = waitingRank;
    }

    public WaitingResponse() {

    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getThemeName() {
        return themeName;
    }

    public String getTimeValue() {
        return timeValue;
    }

    public Long getWaitingRank() {
        return waitingRank;
    }

    public void setWaitingRank(Long rank) {
        this.waitingRank = waitingRank;
    }
}