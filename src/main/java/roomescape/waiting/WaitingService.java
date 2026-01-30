package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.FailMessage;
import roomescape.exception.NotFoundException;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public WaitingResponse save(WaitingRequest waitingRequest, Member member) {
        Time time = timeRepository.findById(waitingRequest.time())
                .orElseThrow(() -> new NotFoundException(FailMessage.NOT_FOUND_TIME));

        Theme theme = themeRepository.findById(waitingRequest.theme())
                .orElseThrow(() -> new NotFoundException(FailMessage.NOT_FOUND_THEME));

        Waiting saved = waitingRepository.save(
                Waiting.memberWaiting(waitingRequest.date(), time, theme, member)
        );

        return new WaitingResponse(
                saved.getId(),
                saved.getTheme().getName(),
                saved.getDate(),
                saved.getTime().getTime());
    }
}
