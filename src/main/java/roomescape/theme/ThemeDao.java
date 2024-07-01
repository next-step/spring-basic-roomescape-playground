package roomescape.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ThemeDao {
    private final ThemeRepository themeRepository;

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }

    public Theme findById(Long id) {
        return themeRepository.findById(id).orElseThrow();
    }
}
