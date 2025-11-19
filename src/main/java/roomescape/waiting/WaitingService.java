package roomescape.waiting;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.ParticipationTime;
import roomescape.time.ParticipationTimeRepository;


@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final ParticipationTimeRepository participationTimeRepository;
    private final MemberRepository memberRepository;

    public WaitingService(WaitingRepository waitingRepository, ThemeRepository themeRepository, ParticipationTimeRepository participationTimeRepository, MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.participationTimeRepository = participationTimeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public WaitingResponse create(WaitingRequest waitingRequest, LoginMember loginMember) {

        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        ParticipationTime participationTime = participationTimeRepository.findById(waitingRequest.time()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));
        Theme theme = themeRepository.findById(waitingRequest.theme()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Waiting waiting = new Waiting(
                member,
                waitingRequest.date(),
                participationTime,
                theme
        );

        waiting = waitingRepository.save(waiting);
        Long rank = waitingRepository.findRankOfWaiting(waiting.getId());
        return new WaitingResponse(waiting.getId(), rank);
    }
}
