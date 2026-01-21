package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorMessage;
import roomescape.exception.NotFoundDataException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findByDeletedFalse();
    }

    @Transactional
    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        Theme theme = themeRepository.findById(id)
                                     .orElseThrow(() -> new NotFoundDataException(ErrorMessage.THEME_NOT_FOUND.getMessage()));
        theme.markDeleted();
    }
}
