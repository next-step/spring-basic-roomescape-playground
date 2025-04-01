package roomescape.waiting;

import java.time.LocalDate;
import roomescape.reservation.view.Formatter;

public record WaitingResponse(Long id, String name, String theme, LocalDate date, String time) {

    public WaitingResponse(Waiting waiting) {
        this(waiting.getId(), waiting.getMemberName(), waiting.getThemeValue(), waiting.getDate()
                , waiting.getTime().format(Formatter.TIME_FORMATTER));
    }
}
