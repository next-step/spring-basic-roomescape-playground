package roomescape.domain.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.repository.ThemeRepository;
import roomescape.global.exception.BadRequestException;
import roomescape.global.exception.ConflictException;
import roomescape.global.exception.NotFoundException;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    private final ReservationRepository reservationRepository;

    public ThemeService(
            ThemeRepository themeRepository,
            ReservationRepository reservationRepository
    ) {
        this.themeRepository = themeRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public Theme saveTheme(String name, String description) {

        if (themeRepository.existsByName(name)) {
            throw new ConflictException("이미 존재하는 테마 이름입니다.");
        }

        Theme theme = new Theme(name, description);

        return themeRepository.save(theme);
    }

    public List<Theme> findAllTheme() {
        return themeRepository.findAll();
    }

    @Transactional
    public void deleteTheme(Long themeId) {
        Theme theme = themeRepository.findById(themeId)
                .orElseThrow(() -> new NotFoundException("해당하는 테마를 찾을 수 없습니다."));

        if (reservationRepository.existsByTheme(theme)) {
            throw new BadRequestException("해당 테마로 예약된 건이 있습니다. 예약을 삭제한 후 다시 시도하여 주세요.");
        }

        themeRepository.deleteById(themeId);
    }
}
