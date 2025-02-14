package roomescape.domain.waiting;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeRepository;
import roomescape.domain.time.Time;
import roomescape.domain.time.TimeRepository;

import java.util.List;

@Service
public class WaitingService {

    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public WaitingService(ThemeRepository themeRepository, TimeRepository timeRepository, MemberRepository memberRepository, WaitingRepository waitingRepository) {
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest, Member loginMember) {

        validateRequest(waitingRequest, loginMember);

        Theme theme = validateTheme(waitingRequest.getTheme());
        Time time = validateTime(waitingRequest.getTime());
        Member member = validateMember(loginMember);

        Waiting waiting = new Waiting(
                waitingRequest.getName(),
                waitingRequest.getDate(),
                time,
                theme,
                member
        );

        waitingRepository.save(waiting);

        List<Waiting> waitings = waitingRepository.findByDateAndTimeIdAndThemeId(
                waiting.getDate(),
                time.getId(),
                theme.getId()
        );

        return new WaitingResponse(
                waiting.getId(),
                waiting.getName(),
                waitingRequest.getTheme(),
                waiting.getDate(),
                waitingRequest.getTime(),
                (long) waitings.size()
        );
    }

    private void validateRequest(WaitingRequest waitingRequest, Member loginMember) {
        if (loginMember == null) {
            throw new IllegalArgumentException("로그인 정보가 필요합니다.");
        }
        if (waitingRequest.getDate() == null || waitingRequest.getTheme() == null || waitingRequest.getTime() == null) {
            throw new IllegalArgumentException("예약 대기 정보가 누락되었습니다.");
        }
    }

    private Theme validateTheme(String themeId) {
        return themeRepository.findById(Long.parseLong(themeId))
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
    }

    private Time validateTime(String timeId) {
        return timeRepository.findById(Long.parseLong(timeId))
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
    }

    private Member validateMember(Member loginMember) {
        return memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 이름을 가진 사용자를 찾을 수 없습니다."));
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
