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

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(
            WaitingRepository waitingRepository,
            ThemeRepository themeRepository,
            TimeRepository timeRepository,
            MemberRepository memberRepository,
            ReservationRepository reservationRepository
    ) {
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public WaitingResponse createWaiting(WaitingRequest waitingRequest, Long memberId) {
        Long themeId = waitingRequest.themeId();
        Long timeId = waitingRequest.timeId();
        String date = waitingRequest.date();

        Theme findTheme = themeRepository.getById(themeId);
        Time findTime = timeRepository.getById(timeId);
        Member member = memberRepository.getById(memberId);

        if (waitingRepository.existsByThemeAndDateAndTimeAndMember(findTheme, date, findTime, member)) {
            throw new IllegalArgumentException("이미 대기열에 등록되어 있습니다.");
        }

        if (reservationRepository.existsByMemberAndThemeAndDateAndTime(member, findTheme, date, findTime)) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 완료되어 대기를 신청할 수 없습니다.");
        }

        Waiting waiting = new Waiting(findTheme, date, findTime, member);
        waitingRepository.save(waiting);
        Long id = waiting.getId();
        Long rank = waitingRepository.countByConditions(findTheme, date, findTime, id);
        return new WaitingResponse(id, rank);
    }

    public void deleteWaiting(Long waitingId, Long memberId) {
        Waiting waiting = waitingRepository.findById(waitingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 대기열이 존재하지 않습니다."));
        if (waiting.isNotSameMember(memberId)) {
            throw new IllegalArgumentException("해당 대기열을 삭제할 권한이 없습니다.");
        }
        waitingRepository.delete(waiting);
    }
}
