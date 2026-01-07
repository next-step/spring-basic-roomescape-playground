package roomescape.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.model.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> { }
