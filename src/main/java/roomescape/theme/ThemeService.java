package roomescape.theme;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse createTheme(ThemeRequest themeRequest) {
        Theme theme = new Theme(null, themeRequest.name(), themeRequest.description());
        Theme savedTheme = themeRepository.save(theme);
        return new ThemeResponse(savedTheme.getId(), savedTheme.getName(), savedTheme.getDescription());
    }

    public List<ThemeResponse> listThemes() {
        return themeRepository.findAll().stream()
                .map(theme -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription()))
                .collect(Collectors.toList());
    }

    public void deleteTheme(Long id) {
        themeRepository.deleteById(id);
    }
}
