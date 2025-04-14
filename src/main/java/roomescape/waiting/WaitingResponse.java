package roomescape.waiting;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record WaitingResponse(long id, String name, String theme, LocalDate date, String time) {

    public static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public WaitingResponse(Waiting waiting) {
        this(waiting.getId(), waiting.getMemberName(), waiting.getThemeValue(), waiting.getDate()
                , waiting.getTime().format(TIME_FORMATTER));
    }
}
