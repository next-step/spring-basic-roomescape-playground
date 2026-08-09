package roomescape.theme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findAllByDeletedAtNull();
}
