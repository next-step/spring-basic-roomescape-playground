package roomescape.theme;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    @NotNull
    @Query("SELECT t FROM Theme t WHERE t.deleted = false")
    List<Theme> findAll();

    @NotNull
    @Query("SELECT t FROM Theme t WHERE t.id = :id AND t.deleted = false")
    Optional<Theme> findById(@NotNull @Param("id") Long id);

}
