package roomescape.theme;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ThemeService {

    private final ThemeRepository themeRepository;

    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    public List<ThemeResponse> findAll() {
        return themeRepository.findAllByDeletedFalse()
                              .stream()
                              .map(theme ->
                                      new ThemeResponse(
                                              theme.getId(),
                                              theme.getName(),
                                              theme.getDescription()
                                      )
                              )
                              .toList();
    }

    public ThemeResponse save(ThemeRequest request) {
        Theme theme = new Theme(request.getName(), request.getDescription());

        Theme savedTheme = themeRepository.save(theme);

        return new ThemeResponse(
                savedTheme.getId(),
                savedTheme.getName(),
                savedTheme.getDescription()
        );
    }

    public void deleteById(Long id) {
        Theme theme = themeRepository
                .findById(id)
                .orElseThrow(NoSuchElementException::new);

        theme.delete();
        themeRepository.save(theme);
    }
}
