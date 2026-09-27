package roomescape.domain.theme.repository;

import org.springframework.data.repository.ListCrudRepository;
import roomescape.domain.theme.entity.Theme;

public interface ThemeRepository extends ListCrudRepository<Theme, Long> {
    boolean existsByName(String name);
}
