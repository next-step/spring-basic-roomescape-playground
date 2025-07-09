package roomescape.theme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Theme t SET t.deleted = true WHERE t.id = :id")
    void deleteById(@Param("id") Long id);
}
