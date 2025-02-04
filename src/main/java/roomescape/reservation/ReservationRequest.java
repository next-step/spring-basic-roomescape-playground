package roomescape.reservation;

public class ReservationRequest {
    private String name;
    private String date;
    private Long theme;
    private Long time;

    public ReservationRequest(String name, String date, Long theme, Long time) {
        validateDate(date);
        validateTheme(theme);
        validateTime(time);
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }

    public void checkName(String name) {
        if (this.name == null) {
            this.name = name;
        }
    }

    private void validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid date");
        }
    }

    private void validateTheme(Long theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Invalid theme");
        }
    }

    private void validateTime(Long time) {
        if (time == null) {
            throw new IllegalArgumentException("Invalid time");
        }
    }
}
