package roomescape.time.dto;

import java.time.LocalTime;

public record AvailableTimeResponse(Long timeId, LocalTime time, boolean booked) {
}
