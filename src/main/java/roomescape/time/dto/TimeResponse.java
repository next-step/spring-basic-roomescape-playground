package roomescape.time.dto;

public class TimeResponse {

    private final Long id;
    private final String value;

    public TimeResponse(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
