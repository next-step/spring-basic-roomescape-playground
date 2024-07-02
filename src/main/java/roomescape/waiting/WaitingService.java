package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.Login.LoginMember;
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
    private WaitingRepository waitingRepository;
    private MemberRepository memberRepository;
    private ThemeRepository themeRepository;
    private TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id()).orElse(null);
        Time time = timeRepository.findById(waitingRequest.time()).orElse(null);
        Theme theme = themeRepository.findById(waitingRequest.theme()).orElse(null);

        List<Waiting> watings = waitingRepository.findByThemeIdAndDateAndTimeId(waitingRequest.theme(), waitingRequest.date(), waitingRequest.time());

        Waiting waiting = waitingRepository.save(new Waiting(member, time, theme, waitingRequest.date()));
        return new WaitingResponse(waiting.getId(), waiting.getTheme().getName(), waiting.getTime().getValue(), waiting.getDate(), watings.size() + 1);
    }

    public void delete(Long id) {
        Waiting waiting = waitingRepository.findById(id).orElse(null);
        if (waiting == null) throw new IllegalArgumentException("삭제할 예약이 없습니다.");
        waitingRepository.delete(waiting);
    }
}
