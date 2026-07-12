package roomescape.time.dto;

public class TimeRequest {

    private final String value;

    public TimeRequest(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
