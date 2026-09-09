package roomescape.reservation;

import roomescape.theme.Theme;
import roomescape.time.Time;

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
        validateFields();
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

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    private void validateFields() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 name은 필수 필드입니다.");
        }

        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 date는 필수 필드입니다.");
        }

        if (time == null) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 time은 필수 필드입니다.");
        }

        if (theme == null) {
            throw new IllegalArgumentException("Reservation을 만들기 위해 theme는 필수 필드입니다.");
        }
    }
}
