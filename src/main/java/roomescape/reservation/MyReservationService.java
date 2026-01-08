package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRankDto;

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

	public List<MyReservationResponseDto> findMine(Long memberId) {
		List<MyReservationResponseDto> result = new ArrayList<>();
        result.addAll(
                reservationRepository.findByMember_Id(memberId).stream()
						.map(MyReservationResponseDto::from)
                        .toList()
        );
		List<WaitingWithRankDto> waitings = waitingService.findMineWithRank(memberId);
		for (WaitingWithRankDto w : waitings) {
			long rankOneBased = (w.rank() == null ? 0 : w.rank()) + 1;
			result.add(new MyReservationResponseDto(
					w.waiting().getId(),
					w.waiting().getTheme().getName(),
					w.waiting().getDate(),
					w.waiting().getTime().getValue(),
                    rankOneBased + "번째 예약대기"
            ));
        }
        return result;
    }
}


