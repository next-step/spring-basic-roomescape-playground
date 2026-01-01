package roomescape.reservation.waiting;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import roomescape.member.Member;
import roomescape.reservation.ReservationRepository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public WaitingService(WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository,
                          ThemeRepository themeRepository,
                          TimeRepository timeRepository) {
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    public WaitingResponse save(ReservationRequest request, Member member) {
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("테마 없음"));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("시간 없음"));

        if (waitingRepository.existsByMemberAndDateTime(member.getId(), request.getDate(), theme.getId(), time.getId())) {
            throw new IllegalArgumentException("이미 해당 시간에 대기/예약이 존재합니다.");
        }

        var existing = reservationRepository.findByDateAndThemeId(request.getDate(), theme.getId());
        boolean hasReservationAtTime = existing.stream()
                .anyMatch(r -> r.getTime().getId().equals(time.getId()));

        if (!hasReservationAtTime) {
            throw new IllegalArgumentException("예약자가 없는 시간에는 대기를 걸 수 없습니다. 바로 예약해 주세요.");
        }

        Waiting waiting = new Waiting(member, theme, time, request.getDate());
        waitingRepository.save(waiting);

        return WaitingResponse.from(waiting);
    }

    public void deleteById(Long id) {
        Waiting waiting = waitingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대기입니다."));
        waitingRepository.delete(waiting);
    }
}