package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.reservation.ReservationRepository;
import roomescape.waiting.WaitingRepository;

import java.util.List;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public ThemeService(ThemeRepository themeRepository, ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme saveTheme = themeRepository.save(themeRequest.toEntity());
        return ThemeResponse.from(saveTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.THEME_NOT_FOUND));

        boolean isExistInReservation = reservationRepository.existsByThemeId(theme.getId());
        boolean isExistInWaiting =waitingRepository.existsByThemeId(theme.getId());

        if (isExistInReservation || isExistInWaiting) {
            throw new RoomEscapeException(ErrorCode.DELETE_CONFLICT, "테마가 다른 자원에서 사용중입니다.");
        }
        theme.softDelete();
    }
}
