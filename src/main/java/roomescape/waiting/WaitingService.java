package roomescape.waiting;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, MemberRepository memberRepository,
                          TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse createWaiting(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = findMemberById(loginMember);
        Time time = findTimeById(waitingRequest);
        Theme theme = findThemeById(waitingRequest);

        List<Waiting> waitings = waitingRepository.findByThemeIdAndDateAndTime(
                waitingRequest.getTheme(), waitingRequest.getDate(), time.getValue()
        );

        validateDuplicateWaitings(waitingRequest, loginMember, waitings, time);

        return saveWaiting(waitingRequest, time, theme, member, waitings);
    }

    public void deleteWaiting(Long id) {
        waitingRepository.deleteById(id);
    }

    private Member findMemberById(LoginMember loginMember) {
        return memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Time findTimeById(WaitingRequest waitingRequest) {
        return timeRepository.findById(waitingRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
    }

    private Theme findThemeById(WaitingRequest waitingRequest) {
        return themeRepository.findById(waitingRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));
    }

    private void validateDuplicateWaitings(WaitingRequest waitingRequest, LoginMember loginMember,
                                           List<Waiting> waitings, Time time) {
        waitings.stream()
                .filter(waiting -> waiting.isMyReservation(loginMember.id()))
                .filter(waiting -> waiting.getTheme().getId().equals(waitingRequest.getTheme()))
                .filter(waiting -> waiting.getDate().equals(waitingRequest.getDate()))
                .filter(waiting -> waiting.getTime().equals(time.getValue()))
                .findAny()
                .ifPresent(waiting -> {
                    throw new IllegalArgumentException(ErrorMessage.ALREADY_WAITING.getMessage());
                });
    }

    private WaitingResponse saveWaiting(WaitingRequest waitingRequest, Time time, Theme theme, Member member,
                                        List<Waiting> waitings) {
        Waiting waiting = waitingRepository.save(new Waiting(waitingRequest.getDate(), time.getValue(), theme, member));
        return new WaitingResponse(waiting.getId(), waiting.getTheme().getId(), waiting.getDate(), waiting.getTime(),
                waitings.size() + 1);
    }
}
