package roomescape.reservation.DTO;

import roomescape.reservation.ReservationStatus;

public class ReservationResponse {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;
    private ReservationStatus status;

    public ReservationResponse(Long id, String name, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationResponse(Long id, String theme, String date, String time, ReservationStatus Status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
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

    public ReservationStatus getStatus() {
        return status;
    }
}
