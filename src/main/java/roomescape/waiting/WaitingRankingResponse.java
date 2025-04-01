package roomescape.waiting;

import java.time.LocalDate;
import roomescape.reservation.view.Formatter;

public record WaitingRankingResponse(long reservationId, String name, String theme, LocalDate date,
                                     String time, long ranking) {

    public WaitingRankingResponse(Waiting waiting, long ranking) {
        this(waiting.getReservation().getId(), waiting.getMemberName(), waiting.getThemeValue(),
                waiting.getDate(),
                waiting.getTime().format(Formatter.TIME_FORMATTER), ranking);
    }
}
