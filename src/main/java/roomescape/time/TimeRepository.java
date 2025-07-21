package roomescape.time;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimeRepository extends JpaRepository<Time, Long> {

    @NotNull
    @Query("SELECT t FROM Time t WHERE t.deleted = false")
    List<Time> findAll();

    @NotNull
    @Query("SELECT t FROM Time t WHERE t.id = :id AND t.deleted = false")
    Optional<Time> findById(@NotNull @Param("id") Long id);

}
