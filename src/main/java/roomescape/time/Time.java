package roomescape.time;

public class Time {
    private Long id;
    private String value;

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Time(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Time을 만들기 위해 value는 필수 필드입니다.");
        }
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }
}
