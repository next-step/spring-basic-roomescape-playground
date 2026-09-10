package roomescape.domain.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ThemeService {

    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @Transactional
    public Theme saveTheme(String name, String description) {
        Theme theme = new Theme(name, description);

        return themeDao.save(theme);
    }

    public List<Theme> findAllTheme() {
        return themeDao.findAll();
    }

    @Transactional
    public void deleteTheme(Long id) {
        themeDao.deleteById(id);
    }
}
