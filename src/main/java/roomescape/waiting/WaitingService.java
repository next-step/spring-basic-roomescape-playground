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

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository,
                          ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public WaitingResponse create(WaitingRequest waitingRequest, String memberEmail) {
        // 예약 할 수 있으면서 대기하는 경우 에외
        Member member = memberRepository.findByEmailOrThrow(memberEmail);
        Time time = timeRepository.findByIdOrThrow(waitingRequest.timeId());
        Theme theme = themeRepository.findByIdOrThrow(waitingRequest.themeId());

        validateDuplicateWaiting(waitingRequest.date(), time, theme, member);

        Waiting waiting = new Waiting(waitingRequest.name(), waitingRequest.date(), time, theme, member);
        Waiting savedWaiting = waitingRepository.save(waiting);
        return WaitingResponse.from(savedWaiting);
    }

    public void validateDuplicateWaiting(String date, Time time, Theme theme, Member member) {
        if (existsByDateAndTimeAndThemeAndMember(date, time, theme, member)) {
            throw new IllegalArgumentException(
                    String.format("Waiting already exist %s, %s, %s", date, time.getValue(), theme.getName()));
        }
    }

    public boolean existsByDateAndTimeAndThemeAndMember(String date, Time time, Theme theme, Member member) {
        return waitingRepository.existsByDateAndTimeAndThemeAndMember(date, time, theme, member);
    }
}
