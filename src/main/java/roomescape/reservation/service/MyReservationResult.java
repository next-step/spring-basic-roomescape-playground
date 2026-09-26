package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;

public record MyReservationResult(
        Long id,
        String theme,
        LocalDate date,
        LocalTime time,
        Status status,
        Long waitingRank
) {
    public enum Status {
        RESERVED,
        WAITING
    }

    public static MyReservationResult reserved(Long id,
                                               String theme,
                                               LocalDate date,
                                               LocalTime time) {
        return new MyReservationResult(id, theme, date, time, Status.RESERVED, null);
    }

    public static MyReservationResult waiting(Long id,
                                              String theme,
                                              LocalDate date,
                                              LocalTime time,
                                              Long waitingRank) {
        return new MyReservationResult(id, theme, date, time, Status.WAITING, waitingRank);
    }
}
