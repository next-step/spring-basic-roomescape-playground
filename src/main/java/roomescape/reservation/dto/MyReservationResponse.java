package roomescape.reservation.dto;

import roomescape.reservation.Reservation;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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

    public static List<MyReservationResponse> of(List<Reservation> reservations,
                                                 List<WaitingWithRank> waitingWithRanks) {
        List<MyReservationResponse> myReservationResponses = reservations.stream()
                .map(it -> new MyReservationResponse(it.getId(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue(),
                        "예약"))
                .collect(toList());

        waitingWithRanks.stream()
                .map(it -> new MyReservationResponse(it.getWaiting().getId(),
                        it.getWaiting().getTheme().getName(),
                        it.getWaiting().getDate(),
                        it.getWaiting().getTime().getValue(),
                        String.format("%d번째 예약대기", it.getRank() + 1)))
                .forEach(myReservationResponses::add);

        return myReservationResponses;
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

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
