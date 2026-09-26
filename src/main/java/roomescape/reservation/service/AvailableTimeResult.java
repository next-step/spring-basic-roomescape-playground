package roomescape.reservation.service;

import java.time.LocalTime;

public record AvailableTimeResult(Long timeId, LocalTime time, boolean booked) {
}
