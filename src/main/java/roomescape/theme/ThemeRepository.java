package roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    List<Theme> findAll();

    Theme save(Theme theme);

    @Modifying
    @Query("update Theme t set t.deleted = true where t.id=:id")
    void deleteById(@Param("id") Long id);
}
