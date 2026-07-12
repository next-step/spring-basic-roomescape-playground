package roomescape.theme.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.entity.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = new Theme(request.name(), request.description());
        Theme savedTheme = themeRepository.save(theme);

        return new ThemeResponse(
                savedTheme.getId(),
                savedTheme.getName(),
                savedTheme.getDescription()
        );
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll()
                .stream()
                .map(theme -> new ThemeResponse(
                        theme.getId(),
                        theme.getName(),
                        theme.getDescription()
                ))
                .toList();
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
