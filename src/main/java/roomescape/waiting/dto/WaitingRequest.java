package roomescape.waiting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record WaitingRequest(
        LocalDate date,
        @JsonProperty("theme") Long themeId,
        @JsonProperty("time") Long timeId
) {
}
