package roomescape.waiting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class WaitingRequest {
    // 검증 로직 추가
    @NotBlank(message = "날짜는 비어있을 수 없습니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "날짜 형식이 올바르지 않습니다. (yyyy-MM-dd)")
    private String date;

    @NotNull(message = "테마는 비어있을 수 없습니다.")
    private Long theme;

    @NotNull(message = "시간은 비어있을 수 없습니다.")
    private Long time;

    public WaitingRequest() {
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
}
