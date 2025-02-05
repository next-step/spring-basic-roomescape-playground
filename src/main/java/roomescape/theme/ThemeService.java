package roomescape.theme;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public Theme findByIdOrThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("Theme not found: " + themeId));
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
