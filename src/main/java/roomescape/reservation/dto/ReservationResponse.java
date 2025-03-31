package roomescape.reservation.dto;

import roomescape.reservation.Status;
import roomescape.reservation.domain.Reservation;
import roomescape.waiting.Waiting;

public class ReservationResponse {
    private Long id;
    private String name;
    private String email;
    private String date;
    private String theme;
    private String time;
    private String status;

    public ReservationResponse(Long id, String name, String email, String theme, String date, String time, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
        this.email = email;
        this.status = status;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(reservation.getId(),
                reservation.getMember().getName(),
                null,
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                null
        );
    }

    public static ReservationResponse from(Reservation reservation, Status status) {
        return new ReservationResponse(reservation.getId(),
                reservation.getMember().getName(),
                reservation.getMember().getEmail(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                status.getDescription()
        );
    }

    public static ReservationResponse from(Waiting waiting, String status) {
        return new ReservationResponse(waiting.getId(),
                null,
                null,
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime(),
                status
        );
    }

    public static ReservationResponse from(Reservation reservation, String status) {
        return new ReservationResponse(reservation.getId(),
                null,
                null,
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                status
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

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }
}
