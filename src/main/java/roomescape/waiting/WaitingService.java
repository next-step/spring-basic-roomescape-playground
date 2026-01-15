package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.error.ErrorCode;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public WaitingResponse createWaiting(ReservationRequest request, Member member) {
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.THEME_NOT_FOUND.getMessage()));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.TIME_NOT_FOUND.getMessage()));

        if (waitingRepository.existsByDateAndTimeAndThemeAndMember(request.getDate(), time, theme, member)) {
            throw new IllegalArgumentException(ErrorCode.WAITING_ALREADY_EXISTS.getMessage());
        }

        Waiting waiting = new Waiting(theme, time, member, request.getDate());
        Waiting savedWaiting = waitingRepository.save(waiting);

        long rank = waitingRepository.countByDateAndTimeAndTheme(request.getDate(), time, theme);

        return new WaitingResponse(
                savedWaiting.getId(),
                savedWaiting.getTheme().getName(),
                savedWaiting.getDate(),
                savedWaiting.getTime().getValue(),
                rank
        );
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
