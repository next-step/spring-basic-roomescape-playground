package roomescape.theme.repository;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import jakarta.persistence.EntityNotFoundException;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Optional<Theme> findById(@NotNull Long themeId);

    default Theme getById(Long themeId) {
        return findById(themeId)
            .orElseThrow(ThemeNotFoundException::withMessage);
    }
}
