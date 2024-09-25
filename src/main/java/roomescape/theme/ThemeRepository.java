package roomescape.theme;

import java.util.NoSuchElementException;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {
    default Theme getById(Long id) {
        return findById(id)
            .orElseThrow(() -> new NoSuchElementException("Theme not found"));
    }
}
