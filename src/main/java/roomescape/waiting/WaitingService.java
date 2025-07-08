package roomescape.waiting;

import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.exception.MemberNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeNotFoundException;
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

    public WaitingResponse save(WaitingRequest waitingRequest) {
        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(TimeNotFoundException::new);
        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(ThemeNotFoundException::new);
        Member member = memberRepository.findByName(waitingRequest.name())
                .orElseThrow(MemberNotFoundException::new);
        Waiting waiting = new Waiting(waitingRequest.name(), waitingRequest.date(), member, time, theme);

        Waiting savedWaiting = waitingRepository.save(waiting);
        List<Waiting> watingList = waitingRepository.findWaitingByDateAndTimeAndTheme(waitingRequest.date(), time, theme);

        return new WaitingResponse(
                savedWaiting.getId(),
                savedWaiting.getName(),
                savedWaiting.getTheme().getName(),
                savedWaiting.getDate(),
                savedWaiting.getTime().getValue(),
                (long) watingList.size()
        );
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }

}
