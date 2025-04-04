package roomescape.reservation;

public class ReservationResponse {
    private final Long id;
    private final String name;
    private final String theme;
    private final String date;
    private final String time;

    public ReservationResponse(Long id, String name, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationResponse(Reservation reservation) {
        this(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
