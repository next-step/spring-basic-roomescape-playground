package roomescape.theme.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.exception.NoSuchThemeException;
import roomescape.theme.model.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description());
        themeRepository.save(theme);
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription());
    }

    public Theme loadThemeEntity(Long id) {
        return themeRepository.findById(id).orElseThrow(NoSuchThemeException::new);
    }

    public List<ThemeResponse> findAll() {
         return themeRepository.findAll().stream()
                .map(theme -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription()))
                .toList();
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }
}
