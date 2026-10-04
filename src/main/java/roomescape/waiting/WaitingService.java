package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository,
                          MemberRepository memberRepository, ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse createWaiting(WaitingRequest request, LoginMember loginMember) {
        if (!reservationRepository.existsByDateAndTimeIdAndThemeId(request.date(), request.time(),request.theme())) {
            throw new IllegalArgumentException("예약이 존재할때만 대기할 수 있습니다.");
        }

        if (waitingRepository.existsByDateAndTimeIdAndThemeIdAndMemberId(
                request.date(), request.time(), request.theme(), loginMember.id())) {
            throw new IllegalArgumentException("이미 대기 중인 예약입니다.");
        }

        Time time = timeRepository.findById(request.time())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(request.theme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Waiting waiting = new Waiting(request.date(), time, theme, member);
        Waiting savedWaiting = waitingRepository.save(waiting);

        return new WaitingResponse(
                savedWaiting.getId(),
                savedWaiting.getDate(),
                savedWaiting.getTime().getValue(),
                savedWaiting.getTheme().getName()
        );
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 대기입니다."));
        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new IllegalArgumentException("본인의 예약 대기만 취소할 수 있습니다.");
        }
        waitingRepository.delete(waiting);
    }

    public List<WaitingWithRank> findWaitingsWithRank(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }
}
