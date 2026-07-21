package roomescape.theme;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<Theme> findAll() {
        return themeDao.findAllByDeletedFalse();
    }

    @Transactional
    public Theme save(Theme theme) {
        return themeDao.save(theme);
    }

    @Transactional
    public void deleteById(Long id) {
        Theme theme = themeDao.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.THEME_NOT_FOUND));
        theme.delete();
    }
}
