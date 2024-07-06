package roomescape.theme;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    default Theme getById(Long id) {
        return this.findById(id)
                   .orElseThrow(() -> new IllegalArgumentException("Theme not found"));
    }
}
