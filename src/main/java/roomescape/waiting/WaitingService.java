package roomescape.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.member.Member;
import roomescape.reservation.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public WaitingService(WaitingRepository waitingRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public WaitingResponse save(ReservationRequest request, Member member) {
        Time time = timeRepository.findById(request.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(request.getTheme()).orElseThrow();

        Waiting waiting = new Waiting(
                request.getDate(),
                time,
                theme,
                member
        );

        Waiting saveWaiting = waitingRepository.save(waiting);

        return new WaitingResponse(
                saveWaiting.getId(),
                theme.getName(),
                saveWaiting.getDate(),
                time.getValue(),
                "예약대기"
        );
    }

    public void delete(Long waitingId, Long memberId) {
        Waiting waiting = waitingRepository.findById(waitingId).orElseThrow();

        if (!waiting.getMember().getId().equals(memberId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        waitingRepository.delete(waiting);
    }

}
