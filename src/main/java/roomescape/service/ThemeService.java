package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.repository.ThemeRepository;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream().map((theme) -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription())).toList();
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = themeRepository.save(new Theme(request.name(), request.description()));

        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription());
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
