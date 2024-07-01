package roomescape.reservation;

import static java.util.stream.Collectors.toList;

import java.util.List;
import roomescape.waiting.WaitingWithRank;

public record MyReservationResponse(Long id,
                                    String theme,
                                    String date,
                                    String time,
                                    String status) {

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
