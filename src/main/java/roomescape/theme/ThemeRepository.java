package roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findAllByDeletedFalse();

    @Modifying
    @Transactional
    @Query("UPDATE Theme t SET t.deleted = true WHERE t.id = :id")
    void softDeleteById(@Param("id") Long id);
}
