package roomescape.theme;

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

    public void deleteById(Long id) {
        Theme theme = themeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다"));
        theme.delete();
        themeRepository.save(theme);
    }
}