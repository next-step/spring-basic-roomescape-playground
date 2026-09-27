package roomescape.theme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.theme.domain.Theme;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAllByDeletedFalse();
    Optional<Theme> findByIdAndDeletedFalse(Long id);
}
