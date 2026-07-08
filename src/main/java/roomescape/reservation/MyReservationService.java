package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.waiting.WaitingService; // WaitingService 주입

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MyReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingService waitingService;

    public MyReservationService(ReservationRepository reservationRepository, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.waitingService = waitingService;
    }

    public List<MyReservationResponse> findMyReservationsAndWaitings(Long memberId) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(memberId).stream()
                .map(MyReservationResponse::from)
                .toList();

        List<MyReservationResponse> waitingResponses = waitingService.findWaitingsWithRankByMemberId(memberId).stream()
                .map(MyReservationResponse::from)
                .toList();

        List<MyReservationResponse> result = new ArrayList<>();
        result.addAll(reservations);
        result.addAll(waitingResponses);

        return result;
    }
}
