package roomescape.theme;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<Theme> findAll() {
        return themeRepository.findAllByDeletedFalse();
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow();

        theme.delete();
    }
}
