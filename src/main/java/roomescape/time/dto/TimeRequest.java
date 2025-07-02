package roomescape.time.dto;

public class TimeRequest {
    private String time;

    private TimeRequest() {
    }

    public TimeRequest(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }
}
