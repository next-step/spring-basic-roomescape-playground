package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.reservation.Reservation;
import roomescape.theme.Theme;
import roomescape.time.Time;
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

    public static MyReservationResponse from(Reservation reservation, Theme theme, Time time) {
        return new MyReservationResponse(
                reservation.getId(),
                theme.getName(),
                reservation.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                time.getTime(),
                "예약"
        );
    }

    public static MyReservationResponse from(WaitingWithRank waitingWithRank, Theme theme, Time time) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                theme.getName(),
                waitingWithRank.getWaiting().getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                time.getTime(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }
}
