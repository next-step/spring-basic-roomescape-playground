package roomescape.waiting;

import java.time.LocalDate;
import java.time.LocalTime;

public class WaitingWithRank {
    private final Long id;
    private final String theme;
    private final LocalDate date;
    private final LocalTime time;
    private final Long rank;

    public WaitingWithRank(Long id,
                           String theme,
                           LocalDate date,
                           LocalTime time,
                           Long rank) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.rank = rank;
    }

    public Long getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Long getRank() {
        return rank;
    }
}
