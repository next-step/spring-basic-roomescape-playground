package roomescape.theme.service;

import org.springframework.stereotype.Service;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResult save(String name, String description) {
        return toResult(themeRepository.save(new Theme(name, description)));
    }

    public List<ThemeResult> findAll() {
        return themeRepository.findAll().stream()
                .map(this::toResult)
                .toList();
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    private ThemeResult toResult(Theme theme) {
        return new ThemeResult(theme.getId(), theme.getName(), theme.getDescription());
    }
}
