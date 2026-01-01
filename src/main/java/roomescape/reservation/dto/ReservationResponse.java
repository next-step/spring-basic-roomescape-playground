package roomescape.reservation.dto;

import roomescape.reservation.Reservation;

public class ReservationResponse {
    private Long id;
    private String name;
    private String theme;
    private String date;
    private String time;

    public ReservationResponse(Long id, String name, String theme, String date, String time) {
        this.id = id;
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(), // Theme 객체에서 이름을 가져옴
                reservation.getDate(),
                reservation.getTime().getValue()  // Time 객체에서 시간 값을 가져옴
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
