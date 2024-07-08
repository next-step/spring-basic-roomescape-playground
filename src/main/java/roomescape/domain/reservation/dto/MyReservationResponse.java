package roomescape.domain.reservation.dto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import roomescape.domain.reservation.constant.ReservationStatus;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.waiting.dto.WaitingWithRank;

public record MyReservationResponse(
        Long id,
        String theme,
        String date,
        String time,
        String status
) {

    public static List<MyReservationResponse> of(final List<Reservation> reservations,
                                                 final List<WaitingWithRank> waitingWithRanks) {
        final Stream<MyReservationResponse> firstStream = reservations
                .stream()
                .map(re -> new MyReservationResponse(re.getId(),
                        re.getTheme().getName(),
                        re.getDate(),
                        re.getTime().getValue(),
                        ReservationStatus.RESERVED.getMessage()));
        final Stream<MyReservationResponse> secondStream = waitingWithRanks.stream()
                .map(wa -> new MyReservationResponse(wa.waiting().getId(),
                        wa.waiting().getTheme().getName(),
                        wa.waiting().getDate(),
                        wa.waiting().getTime().getValue(),
                        ReservationStatus.WAITING.getFormattedMessage(wa.rank() + 1)));
        return Stream.concat(firstStream,secondStream).collect(Collectors.toList());
    }
}
