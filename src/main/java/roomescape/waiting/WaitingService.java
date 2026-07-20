package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.login.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(WaitingRepository waitingRepository,ThemeRepository themeRepository,TimeRepository timeRepository,MemberRepository memberRepository){
        this.waitingRepository=waitingRepository;
        this.themeRepository=themeRepository;
        this.timeRepository=timeRepository;
        this.memberRepository=memberRepository;
    }

    public List<WaitingWithRank> findWaiting(Long id){
        return waitingRepository.findWaitingsWithRankByMemberId(id);
    }

    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember){
        Theme theme= themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow();
        Time time=timeRepository.findById(waitingRequest.getTime())
                .orElseThrow();
        Member member=memberRepository.findById(loginMember.getId())
                .orElseThrow();

        Waiting waiting = new Waiting(
                member,
                waitingRequest.getDate(),
                theme,
                time
        );

        waitingRepository.save(waiting);

        return new WaitingResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                waiting.getTime().getTimeValue()
        );
    }
}
