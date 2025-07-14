package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.reservation.ReservationRepository;
import roomescape.waiting.WaitingRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme saveTheme = themeRepository.save(themeRequest.toEntity());
        return ThemeResponse.from(saveTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));

        boolean isExistInReservation = reservationRepository.existsByThemeId(theme.getId());
        boolean isExistInWaiting =waitingRepository.existsByThemeId(theme.getId());

        if (isExistInReservation || isExistInWaiting) {
            throw new RoomEscapeException(ErrorCode.DELETE_CONFLICT, "해당 테마는 예약 또는 예약 대기 목록에 사용 중입니다.");
        }
        themeRepository.deleteById(id);
    }
}
