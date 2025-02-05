package roomescape.theme;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ThemeRepository extends CrudRepository<Theme, Long> {
    List<Theme> findAll();
}
