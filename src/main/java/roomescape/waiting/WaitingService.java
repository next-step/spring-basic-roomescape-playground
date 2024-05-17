package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingResponse save(LoginMember loginMember, WaitingRequest waitingRequest) {

        Member member = memberRepository.findById(loginMember.getId()).get();
        Time time = timeRepository.findById(waitingRequest.getTime()).get();
        Theme theme = themeRepository.findById(waitingRequest.getTheme()).get();

        Waiting waiting = waitingRepository.save(new Waiting(null, member, waitingRequest.getDate(), time, theme));

        //TODO 예약 대기 순서 표시
        return new WaitingResponse(waiting.getId(), theme.getName(), waitingRequest.getDate(), time.getTime_value(), 1);
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
