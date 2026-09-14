package roomescape.domain.theme.repository;

import roomescape.domain.theme.entity.Theme;

import java.util.List;

public interface ThemeRepository {

    List<Theme> findAll();

    Theme save(Theme theme);

    void deleteById(Long themeId);
}
