package roomescape.reservation;

public class ReservationRequest {
    private final String name;
    private final String date;
    private final Long theme;
    private final Long time;

    public ReservationRequest(String name, String date, Long theme, Long time) {
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

    public ReservationRequest withUserName(String userName) {
        if (this.name == null) {
            return new ReservationRequest(
                    userName,
                    getDate(),
                    getTheme(),
                    getTime()
            );
        }
        return this;
    }
}
