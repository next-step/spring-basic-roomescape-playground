package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginMember;
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
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository,
        ThemeRepository themeRepository, MemberRepository memberRepository,
        ReservationRepository reservationRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse create(WaitingRequest request, LoginMember loginMember) {
        Time time = timeRepository.findById(request.getTime())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(request.getTheme())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Member member = memberRepository.findById(loginMember.id())
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        boolean isReserved = reservationRepository.existsByMemberAndDateAndTimeAndTheme(member,
            request.getDate(), time, theme);
        boolean isWaiting = waitingRepository.existsByMemberAndDateAndTimeAndTheme(member,
            request.getDate(), time, theme);

        if (isReserved || isWaiting) {
            throw new IllegalArgumentException("이미 예약 또는 예약 대기 상태입니다.");
        }

        Waiting waiting = new Waiting(member, theme, time, request.getDate());
        Waiting savedWaiting = waitingRepository.save(waiting);
        return new WaitingResponse(savedWaiting.getId());
    }

    public void delete(Long waitingId, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(waitingId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 대기입니다."));

        if (!waiting.getMember().getId().equals(loginMember.id())) {
            throw new SecurityException("자신의 예약 대기만 취소할 수 있습니다.");
        }
        waitingRepository.delete(waiting);
    }
}
