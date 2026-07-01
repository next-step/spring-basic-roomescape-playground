package roomescape.theme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public ThemeResponse findById(Long id) {
        Theme theme = themeRepository.findById(id).orElseThrow(NoSuchThemeException::new);
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription());
    }

    public Theme findEntityById(Long id) {
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
