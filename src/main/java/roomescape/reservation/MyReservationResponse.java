package roomescape.reservation;

import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Collectors;

public class MyReservationResponse {

    private Long id;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long id, String theme, String date, String time, String status) {
        this.id = id;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public Long getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public static List<MyReservationResponse> from(List<ReservationResponse> reservationResponses) {
        return reservationResponses.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(), reservation.getTheme(), reservation.getDate(), reservation.getTime(), "예약"))
                .collect(Collectors.toList());
    }

    public static List<MyReservationResponse> of(List<Reservation> reservations, List<WaitingWithRank> waitingWithRanks) {
        List<MyReservationResponse> myReservationResponses = reservations.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        "예약"))
                .collect(Collectors.toList());

        waitingWithRanks.stream().map(reservation -> new MyReservationResponse(
                reservation.getWaiting().getId(),
                reservation.getWaiting().getTheme().getName(),
                reservation.getWaiting().getDate(),
                reservation.getWaiting().getTime().getValue(),
                String.format("%d번째 예약대기", reservation.getRank() + 1)))
                .forEach(myReservationResponses::add);

        return myReservationResponses;

    }
}
