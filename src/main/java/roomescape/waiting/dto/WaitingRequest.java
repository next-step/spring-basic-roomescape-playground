package roomescape.waiting.dto;

import lombok.Getter;

import java.time.LocalDate;

public class WaitingRequest {

    @Getter
    private LocalDate date;
    @Getter
    private Long timeId;
    @Getter
    private Long themeId;

    private WaitingRequest() {}

    public WaitingRequest(LocalDate date, Long timeId, Long themeId) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }
}
