package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.reservation.Reservation;
import roomescape.waiting.dto.WaitingWithRank;

import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class MyReservationResponse {

    private final Long reservationId;
    private final String theme;
    private final String date;
    private final String time;
    private final String status;

    public static MyReservationResponse from(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                reservation.getTime().getTime(),
                "예약"
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                waitingWithRank.getWaiting().getTime().getTime(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }

    public Long getId() {
        return reservationId;
    }
}
