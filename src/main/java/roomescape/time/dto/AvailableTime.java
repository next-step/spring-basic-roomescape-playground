package roomescape.time.dto;

import roomescape.reservation.entity.Reservation;
import roomescape.time.entity.Time;

import java.util.List;

public record AvailableTime(
        Long id,
        String time,
        boolean booked
) {

    public static AvailableTime of(Time time, List<Reservation> reservations) {
        boolean alreadyBooked = reservations.stream()
                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()));

        return new AvailableTime(
                time.getId(),
                time.getTimeValue(),
                alreadyBooked
        );
    }
}
