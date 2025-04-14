package roomescape.waiting;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record WaitingRankingResponse(long reservationId, String name, String theme, LocalDate date,
                                     String time, long ranking) {

    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public WaitingRankingResponse(Waiting waiting, long ranking) {
        this(waiting.getReservation().getId(), waiting.getMemberName(), waiting.getThemeValue(),
                waiting.getDate(),
                waiting.getTime().format(TIME_FORMATTER), ranking);
    }
}
