package roomescape.theme;

import org.springframework.stereotype.Service;
import roomescape.reservation.ReservationRepository;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ReservationRepository reservationRepository;

    public ThemeService(final ThemeRepository themeRepository, final ReservationRepository reservationRepository) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }


    public ThemeResponse validateAndSave(ThemeRequest request) {
        Theme theme = Theme.of(request.name(), request.description());

        if (themeRepository.existsByName(theme.getName())) {
            throw new IllegalArgumentException("이미 존재하는 이름의 테마입니다.");
        }

        theme = themeRepository.save(theme);
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription());
    }


    public List<ThemeResponse> getAllThemes() {
        List<Theme> themes = themeRepository.findAll();
        return themes.stream()
                .map(theme -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription())).toList();
    }

    public void validateAndDeleteTheme(Long id) {

        if (reservationRepository.existsByThemeId(id)) {
            throw new IllegalArgumentException("해당 테마를 예약하는 예약이 존재합니다.");
        }
        themeRepository.deleteById(id);

    }

}
