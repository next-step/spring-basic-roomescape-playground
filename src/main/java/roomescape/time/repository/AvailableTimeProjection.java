package roomescape.time.repository;

import java.time.LocalTime;

public record AvailableTimeProjection(Long timeId, LocalTime time, boolean booked) {
}
