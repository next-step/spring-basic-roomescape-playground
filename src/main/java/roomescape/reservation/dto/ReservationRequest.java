package roomescape.reservation.dto;

public class ReservationRequest {

    public ReservationRequest() {
    }

    public ReservationRequest(final String name, final String date, final Long theme,
                              final Long time) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    private String name;
    private String date;
    private Long theme;
    private Long time;

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
}
