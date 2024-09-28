package roomescape.theme;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface ThemeRepository extends Repository<Theme, Long> {

    List<Theme> findAll();

    Optional<Theme> findById(Long id);

    default Theme getById(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException("Theme not found"));
    }

    Theme save(Theme theme);

    void deleteById(Long id);
}
