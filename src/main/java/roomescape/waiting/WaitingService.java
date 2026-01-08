package roomescape.waiting;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final EntityManager entityManager;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          EntityManager entityManager) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.entityManager = entityManager;
    }

	@Transactional
	public WaitingResponseDto create(Long memberId, String date, Long timeId, Long themeId) {
        if (reservationRepository.existsForMemberOnSlot(memberId, date, timeId, themeId)) {
            throw new IllegalStateException();
        }
        if (waitingRepository.existsForMemberOnSlot(memberId, date, timeId, themeId)) {
            throw new IllegalStateException();
        }

        Member memberRef = entityManager.getReference(Member.class, memberId);
        Time timeRef = entityManager.getReference(Time.class, timeId);
        Theme themeRef = entityManager.getReference(Theme.class, themeId);
        Waiting waiting = new Waiting(memberRef, date, timeRef, themeRef);
		waiting = waitingRepository.save(waiting);
		return new WaitingResponseDto(waiting.getId());
    }

	@Transactional
    public void cancel(Long waitingId, Long memberId) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow();
        if (!waiting.getMember().getId().equals(memberId)) {
            throw new IllegalStateException();
        }
        waitingRepository.deleteById(waitingId);
    }

	public List<WaitingWithRankDto> findMineWithRank(Long memberId) {
        var mine = waitingRepository.findByMember_IdOrderByIdAsc(memberId);
        return mine.stream()
				.map(w -> new WaitingWithRankDto(
                        w,
                        waitingRepository.countByTheme_IdAndDateAndTime_IdAndIdLessThan(
                                w.getTheme().getId(), w.getDate(), w.getTime().getId(), w.getId()))
                )
                .toList();
    }
}


