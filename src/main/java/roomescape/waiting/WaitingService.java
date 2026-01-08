package roomescape.waiting;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("시간을 찾을 수 없습니다."));

        if (waitingRepository.existsByDateAndTimeAndThemeAndMember(request.getDate(), time, theme, member)) {
            throw new IllegalArgumentException("이미 대기 중입니다.");
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
