package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberResponse;
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

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse save(LoginMember loginMember, WaitingRequest waitingRequest) {

        Member member = memberRepository.findById(loginMember.getId()).get();
        Time time = timeRepository.findById(waitingRequest.getTime()).get();
        Theme theme = themeRepository.findById(waitingRequest.getTheme()).get();

        Waiting waiting = waitingRepository.save(new Waiting(null, member, waitingRequest.getDate(), time, theme));

        return new WaitingResponse(waiting.getId(), theme.getName(), waitingRequest.getDate(), time.getTime_value(), 1);
    }

    public void deleteWaiting(Long id, MemberResponse memberResponse) {
        Waiting waiting = waitingRepository.findById(id).orElseThrow(RuntimeException::new);
        if (!waiting.isMyReservation(memberResponse.getId())) {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }
        waitingRepository.deleteById(id);
    }

}
