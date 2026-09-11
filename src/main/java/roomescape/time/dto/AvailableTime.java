package roomescape.time.dto;

import java.time.LocalTime;

public record AvailableTime(Long timeId, LocalTime time, boolean booked) {
}
