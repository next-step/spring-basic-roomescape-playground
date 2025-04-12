package roomescape.theme;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    default Theme getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 대기열이 존재하지 않습니다."));
    }

    List<Theme> findAllByIsDeletedFalse();
}
