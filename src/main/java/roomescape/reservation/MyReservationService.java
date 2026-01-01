package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyReservationService {
    private final ReservationRepository reservationRepository;
    private final WaitingService waitingService;

    public MyReservationService(ReservationRepository reservationRepository, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.waitingService = waitingService;
    }

    public List<MyReservationResponse> findMine(Long memberId) {
        List<MyReservationResponse> result = new ArrayList<>();
        result.addAll(
                reservationRepository.findByMember_Id(memberId).stream()
                        .map(MyReservationResponse::from)
                        .toList()
        );
        List<WaitingWithRank> waitings = waitingService.findMineWithRank(memberId);
        for (WaitingWithRank w : waitings) {
            long rankOneBased = (w.getRank() == null ? 0 : w.getRank()) + 1;
            result.add(new MyReservationResponse(
                    w.getWaiting().getId(),
                    w.getWaiting().getTheme().getName(),
                    w.getWaiting().getDate(),
                    w.getWaiting().getTime().getValue(),
                    rankOneBased + "번째 예약대기"
            ));
        }
        return result;
    }
}


