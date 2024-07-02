package roomescape.domain.theme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.theme.entity.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
}
