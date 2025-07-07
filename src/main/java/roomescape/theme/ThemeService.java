package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public ThemeResponse save(ThemeRequest themeRequest) {
        Theme saveTheme = themeRepository.save(themeRequest.toEntity());
        return ThemeResponse.from(saveTheme);
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAll().stream()
                .map(ThemeResponse::from)
                .toList();
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
