package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingWithRank;

public record MyReservationResponse(
        @JsonProperty("id")
        Long reservationId,
        String theme,
        String date,
        String time,
        String status
) {
        public static MyReservationResponse from(Reservation reservation) {
                return new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        "예약"
                );
        }

        public static MyReservationResponse from(WaitingWithRank waitingWithRank) {
                Waiting waiting = waitingWithRank.getWaiting();
                Long rank = waitingWithRank.getRank() + 1;
                return new MyReservationResponse(
                        waiting.getId(),
                        waiting.getTheme().getName(),
                        waiting.getDate(),
                        waiting.getTime().getValue(),
                        rank + "번째 예약대기"
                );
        }
}
