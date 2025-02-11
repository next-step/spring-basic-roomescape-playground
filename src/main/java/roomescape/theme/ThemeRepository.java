package roomescape.theme;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ThemeRepository extends CrudRepository<Theme, Long> {

    default Theme findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException(String.format("Theme Not found: %d", id)));
    }

    List<Theme> findAll();
}
