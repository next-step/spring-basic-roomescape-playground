package roomescape.reservation;

import roomescape.exception.InvalidReservationException;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private Time time;
    private Theme theme;

    public Reservation(Long id, String name, String date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, String date, Time time, Theme theme) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public static Reservation create(
            String name,
            String date,
            Time time,
            Theme theme
    ) {
        validateFutureDateTime(date, time);

        return new Reservation(name, date, time, theme);
    }

    private static void validateFutureDateTime(String date, Time time) {
        try {
            LocalDateTime reservationDateTime = LocalDateTime.of(
                    LocalDate.parse(date),
                    LocalTime.parse(time.getValue())
            );

            if (!reservationDateTime.isAfter(LocalDateTime.now())) {
                throw new InvalidReservationException(
                        "올바른 예약 날짜와 시간을 선택해야 합니다."
                );
            }
        } catch (DateTimeParseException exception) {
            throw new InvalidReservationException(
                    "올바른 예약 날짜와 시간을 선택해야 합니다."
            );
        }
    }
}
