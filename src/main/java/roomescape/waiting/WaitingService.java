package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.dto.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

import java.util.List;

@Service
public class WaitingService {
    private MemberRepository memberRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;

    public WaitingService(MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository, WaitingRepository waitingRepository) {
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public WaitingResponse save(WaitingRequest request, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElse(null);
        Time time = timeRepository.findById(request.time()).orElse(null);
        Theme theme = themeRepository.findById(request.theme()).orElse(null);

        Waiting waiting = waitingRepository.save(new Waiting(
                member,
                time,
                theme,
                request.date()
        ));

        List<Waiting> waitingList = waitingRepository.findByThemeIdAndDateAndTimeId(
                request.theme(),
                request.date(),
                request.time()
        );

        return new WaitingResponse(
                waiting.getId(),
                waiting.getDate(),
                waiting.getTime().getValue(),
                waiting.getTheme().getName(),
                (long) waitingList.size() + 1
        );
    }
}
