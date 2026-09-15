package roomescape.reservation;

import jakarta.validation.constraints.NotNull;

public class ReservationRequest {
    private String name;

    @NotNull(message = "예약 날짜는 비어 있을 수 없습니다.")
    private String date;

    @NotNull(message = "예약 테마는 비어 있을 수 없습니다.")
    private Long theme;

    @NotNull(message = "예약 시간은 비어 있을 수 없습니다.")
    private Long time;

    public String getName() {
        return name;
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
