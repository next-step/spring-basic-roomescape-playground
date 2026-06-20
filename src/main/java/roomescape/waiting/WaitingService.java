package roomescape.waiting;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtUtils;
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
    private final JwtUtils jwtUtils;

    public WaitingService(WaitingRepository waitingRepository,
                          TimeRepository timeRepository,
                          ThemeRepository themeRepository,
                          JwtUtils jwtUtils) {
        this.waitingRepository = waitingRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.jwtUtils = jwtUtils;
    }

    public WaitingResponse save(ReservationRequest request, String token) {
        Long memberId = Long.parseLong(jwtUtils.getMemberId(token));

        Time time = timeRepository.findById(request.time())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다"));
        Theme theme = themeRepository.findById(request.theme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다"));

        Waiting waiting = new Waiting(memberId, request.date(), time, theme);
        Waiting saved = waitingRepository.save(waiting);

        return new WaitingResponse(saved.getId(), saved.getTheme().getName(), saved.getDate(), saved.getTime().getTime());
    }

    public void deleteById(Long id, String token) {
        Long memberId = Long.parseLong(jwtUtils.getMemberId(token));

        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 예약 대기입니다"));

        if (!waiting.getMemberId().equals(memberId)) {
            throw new IllegalArgumentException("본인의 예약 대기만 취소할 수 있습니다");
        }

        waitingRepository.deleteById(id);
    }
}
