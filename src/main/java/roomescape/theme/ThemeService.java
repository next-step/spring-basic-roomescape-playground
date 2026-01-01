package roomescape.theme;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
@Transactional
public class ThemeService {

    private ThemeRepository themeRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Transactional
    public Theme save(Theme theme) {
        return themeRepository.save(theme);
    }

    public List<Theme> findAll() {
        return themeRepository.findAll();
    }

    @Transactional
    public void deleteById(Long id) {
        themeRepository.deleteById(id);
    }
}
