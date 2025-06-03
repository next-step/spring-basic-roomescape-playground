package roomescape.theme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAllByDeletedFalse();

    @Modifying
    @Query("UPDATE Theme t SET t.deleted = true WHERE t.id = :id")
    void softDeleteById(Long id);
}
