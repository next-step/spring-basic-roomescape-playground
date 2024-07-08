package roomescape.waiting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import roomescape.annotation.MemberSession;
import roomescape.member.MemberRepository;
import roomescape.member.model.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class WaitingService {
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private TimeRepository timeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/waitings")
    public WaitingResponse save(WaitingRequest request, Long memberId) {
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Member member = memberRepository.findById(memberId).orElseThrow();

        Waiting waiting = waitingRepository.save(new Waiting(request.getDate(), time, theme, member));
        Long rank = waitingRepository.findWaitingsWithRankByMemberId(member.getId()).stream().filter(it -> it.getWaiting().getId().equals(waiting.getId())).findFirst().get().getRank();

        return new WaitingResponse(waiting.getId(), waiting.getDate(), time.getValue(), theme.getName(), member.getId(), rank + 1);
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}