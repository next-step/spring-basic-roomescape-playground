package roomescape.domain.reservation;

import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;

import java.time.LocalDate;

public class Reservation {
    private Long id;
    private String name;
    private LocalDate date;
    private Time time;
    private Theme theme;

    public Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Reservation(String name, LocalDate date, Time time, Theme theme) {
        validateFields(name, date, time, theme);
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    private void validateFields(String name, LocalDate date, Time time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 name은 필수 필드입니다.");
        }

        if (date == null) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 date는 필수 필드입니다.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Reservation은 현재, 혹은 과거의 날짜로 생성할 수 없습니다.");
        }

        if (time == null) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 time은 필수 필드입니다.");
        }

        if (theme == null) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 theme는 필수 필드입니다.");
        }
    }
}
