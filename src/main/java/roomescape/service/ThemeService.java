package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ThemeDao;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.model.Theme;

@Service
public class ThemeService {
    private final ThemeDao themeDao;

    public ThemeService(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    public List<ThemeResponse> findAll() {
        return themeDao.findAll().stream().map((theme) -> new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription())).toList();
    }

    public ThemeResponse create(ThemeRequest request) {
        Theme theme = themeDao.save(new Theme(request.name(), request.description()));

        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription());
    }

    public void deleteById(Long id) {
        themeDao.deleteById(id);
    }
}
