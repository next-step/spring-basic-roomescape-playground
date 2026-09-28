package roomescape.waiting;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberNotFoundException;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
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

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.getId()).orElseThrow(MemberNotFoundException::new);
        String date = waitingRequest.date();
        Time time = timeRepository.findById(waitingRequest.time()).orElseThrow();
        Theme theme = themeRepository.findById(waitingRequest.theme()).orElseThrow();

        Waiting waiting = new Waiting(member, date, time, theme);

        Waiting savedWaiting = waitingRepository.save(waiting);

        long earlierCount = waitingRepository.countEarlierWaitings(savedWaiting.getTheme(), savedWaiting.getDate(),savedWaiting.getTime(), savedWaiting.getId());
        long waitingNumber = earlierCount + 1;

        return new WaitingResponse(savedWaiting.getId(), waitingNumber);
    }

    @Transactional
    public void deleteById(Long id, LoginMember loginMember) {
        Waiting waiting = waitingRepository.findById(id).orElseThrow();

        if (!waiting.getMember().getId().equals(loginMember.getId())) {
            throw new WaitingAccessDeniedException();
        }

        waitingRepository.delete(waiting);
    }
}
