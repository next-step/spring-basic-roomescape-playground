package roomescape.domain.reservation.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.waiting.entity.ReserveWaiting;
import roomescape.domain.waiting.service.result.WaitingWithRank;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationsResponse(
        Long id,
        String theme,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @JsonFormat(pattern = "HH:mm")
        LocalTime time,
        String status
) {
    public static MyReservationsResponse from(Reservation reservation) {
        return new MyReservationsResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getTimeValue(),
                "예약"
        );
    }

    public static MyReservationsResponse from(WaitingWithRank waitingWithRank) {
        ReserveWaiting waiting = waitingWithRank.reserveWaiting();
        return new MyReservationsResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getTimeValue(),
                waitingWithRank.rank() + "번째 예약대기"
        );
    }
}
