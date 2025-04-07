package roomescape.waiting.domain;

import java.time.LocalDate;
import java.time.LocalTime;

public class WaitingWithRank {

    private final Waiting waiting;
    private final Long rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank;
    }

    public Long getWaitingId() {
        return waiting.getId();
    }

    public LocalDate getDate() {
        return waiting.getDate();
    }

    public LocalTime getTimeValue() {
        return waiting.getTimeValue();
    }

    public String getThemeName() {
        return waiting.getThemeName();
    }

    public String getRankStatus() {
        return Status.WAITING.getDescriptionWith(rank);
    }
}
