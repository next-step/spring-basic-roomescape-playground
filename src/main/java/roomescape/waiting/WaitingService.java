package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
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
    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest) {

        Time time = timeRepository.findById(waitingRequest.getTime()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(waitingRequest.getTheme()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Member member = memberRepository.findByName(waitingRequest.getName()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        Waiting waiting = new Waiting(waitingRequest.getDate(), time, theme, member);
        waitingRepository.save(waiting);

        return new WaitingResponse(waiting.getId(), waiting.getMember().getName(), waiting.getTheme().getName(), waiting.getDate(), waiting.getTime().getValue());
    }
}
