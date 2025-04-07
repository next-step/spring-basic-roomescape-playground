package roomescape.reservation.dto;

import roomescape.reservation.Status;
import roomescape.reservation.domain.Reservation;
import roomescape.waiting.domain.Waiting;

public class ReservationResponse {
    private Long id;
    private Long waitingId;
    private String name;
    private String email;
    private String date;
    private String theme;
    private String time;
    private String status;

    public ReservationResponse(Long id, Long waitingId, String name, String email, String theme, String date, String time, String status) {
        this.id = id;
        this.waitingId = waitingId;
        this.name = name;
        this.email = email;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                null,
                reservation.getMember().getName(),
                null,
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                null
        );
    }

    public static ReservationResponse from(Reservation reservation, Status status) {
        return new ReservationResponse(
                reservation.getId(),
                null,
                reservation.getMember().getName(),
                reservation.getMember().getEmail(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                status.getDescription()
        );
    }

    public static ReservationResponse from(Reservation reservation, Waiting waiting, Status status) {
        return new ReservationResponse(
                reservation.getId(),
                waiting.getId(),
                waiting.getMember().getName(),
                waiting.getMember().getEmail(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime(),
                status.getDescription()
        );
    }

    public static ReservationResponse from(Waiting waiting, String status) {
        return new ReservationResponse(
                waiting.getId(),
                null,
                null,
                null,
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime(),
                status
        );
    }

    public static ReservationResponse from(Reservation reservation, String status) {
        return new ReservationResponse(
                reservation.getId(),
                null,
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

    public Long getWaitingId() {
        return waitingId;
    }
}
