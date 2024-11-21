package roomescape.reservation;

import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingWithRank;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MyReservationResponse {
    private Long reservationId;
    private String theme;
    private String date;
    private String time;
    private String status;

    public MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {
        this.reservationId = reservationId;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public MyReservationResponse() {

    }

    public Long getReservationId() {
        return reservationId;
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
}
