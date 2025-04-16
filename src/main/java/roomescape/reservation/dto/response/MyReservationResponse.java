package roomescape.reservation.dto.response;

import roomescape.reservation.domain.Reservation;
import roomescape.waiting.domain.WaitingWithRank;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationResponse(
        long id,
        String theme,
        LocalDate date,
        LocalTime time,
        String status
) {
    public MyReservationResponse(Reservation reservation) {
        this(reservation.getId(),
                reservation.getThemeName(),
                reservation.getDate(),
                reservation.getTimeValue(),
                reservation.getRankStatus()
        );
    }

    public MyReservationResponse(WaitingWithRank waitingWithRank) {
        this(waitingWithRank.getWaitingId(),
                waitingWithRank.getThemeName(),
                waitingWithRank.getDate(),
                waitingWithRank.getTimeValue(),
                waitingWithRank.getRankStatus()
        );
    }
}
