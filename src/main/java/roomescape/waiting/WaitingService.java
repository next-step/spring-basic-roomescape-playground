package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
	private final MemberRepository memberRepository;
	private final TimeRepository timeRepository;
	private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
						  MemberRepository memberRepository,
						  TimeRepository timeRepository,
						  ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
		this.memberRepository = memberRepository;
		this.timeRepository = timeRepository;
		this.themeRepository = themeRepository;
    }

	@Transactional
	public WaitingResponseDto create(Long memberId, String date, Long timeId, Long themeId) {
        if (reservationRepository.existsForMemberOnSlot(memberId, date, timeId, themeId)) {
            throw new IllegalStateException();
        }
        if (waitingRepository.existsForMemberOnSlot(memberId, date, timeId, themeId)) {
            throw new IllegalStateException();
        }

		Member memberRef = memberRepository.getReferenceById(memberId);
		Time timeRef = timeRepository.getReferenceById(timeId);
		Theme themeRef = themeRepository.getReferenceById(themeId);
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


