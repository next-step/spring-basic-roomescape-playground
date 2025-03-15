package roomescape.reservation.dto.response;

import roomescape.reservation.domain.Reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;

    protected ReservationResponse(Long id, String name, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName();
        this.theme = reservation.getTheme().getName();
        this.date = reservation.getDate();
        this.time = reservation.getTime().getValue();
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
