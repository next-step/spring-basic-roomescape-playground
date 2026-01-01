package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          MemberRepository memberRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository
    ) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public WaitingResponse createWaiting(LoginMember loginMember, ReservationRequest request) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme());
        Time time = timeRepository.findById(request.getTime()).orElseThrow();

        Waiting waiting = new Waiting(member, theme, time, request.getDate());
        waitingRepository.save(waiting);

        return new WaitingResponse(waiting.getId(), theme.getName(), waiting.getDate(), time.getTime());
    }

    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }
}
