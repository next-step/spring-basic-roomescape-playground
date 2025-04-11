package roomescape.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import roomescape.waiting.WaitingRankingResponse;

public record MemberReservationResponse(long id, String theme, LocalDate date
        , String time, String status) {

    private static final String WAITING_STATUS_MESSAGE = "%d번째 예약대기";
    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public MemberReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getThemeValue(),
                reservation.getDate(),
                reservation.getTimeValue().format(TIME_FORMATTER),
                "예약"
        );
    }

    public MemberReservationResponse(WaitingRankingResponse response) {
        this(response.reservationId(),
                response.theme(),
                response.date(),
                response.time(),
                WAITING_STATUS_MESSAGE.formatted(response.ranking()));
    }
}
