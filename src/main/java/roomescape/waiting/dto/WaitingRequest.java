package roomescape.waiting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WaitingRequest {

    @NotBlank(message = "날짜는 필수입니다.")
    private String date;

    @NotNull(message = "테마는 필수입니다.")
    private Long theme;

    @NotNull(message = "시간은 필수입니다.")
    private Long time;

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
