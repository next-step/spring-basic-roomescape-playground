package roomescape.reservation.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.Waiting;
import roomescape.reservation.dto.WaitingWithRank;

public record MyReservationResponse(
    Long id,
    String theme,
    LocalDate date,
    @JsonFormat(pattern = "HH:mm")
    LocalTime time,
    String status
) {
    public static MyReservationResponse fromReservation(Reservation reservation) {
        return new MyReservationResponse(
            reservation.getId(),
            reservation.getTheme().getName(),
            LocalDate.parse(reservation.getDate()),
            LocalTime.parse(reservation.getTime().getTime()),
            "예약"
        );
    }

    public static MyReservationResponse fromWaitingWithRank(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.waiting();

        return new MyReservationResponse(
            waiting.getId(),
            waiting.getTheme().getName(),
            LocalDate.parse(waiting.getDate()),
            LocalTime.parse(waiting.getTime().getTime()),
            String.format("%d번째 예약대기", waitingWithRank.rank() + 1)
        );
    }

    public static List<MyReservationResponse> from(List<Reservation> reservations, List<WaitingWithRank> waiting) {
        List<MyReservationResponse> responses = new ArrayList<>();

        responses.addAll(reservations.stream()
            .map(MyReservationResponse::fromReservation)
            .toList());

        responses.addAll(waiting.stream()
            .map(MyReservationResponse::fromWaitingWithRank)
            .toList());

        responses.sort(Comparator.comparing(MyReservationResponse::date)
            .thenComparing(MyReservationResponse::time));

        return responses;
    }
}
