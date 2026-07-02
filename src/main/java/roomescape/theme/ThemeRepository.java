package roomescape.theme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAllByDeletedFalse();

    @Transactional
    @Modifying
    @Query("update Theme theme set theme.deleted = true where theme.id = :id")
    void softDeleteById(Long id);
}
