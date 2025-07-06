package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Theme> findAll() {
        return entityManager.createQuery("SELECT t FROM Theme t WHERE t.deleted = false", Theme.class)
                .getResultList();
    }

    public Optional<Theme> findById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        return Optional.ofNullable(theme);
    }

    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    public void deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        if (theme == null) {
            throw new RoomEscapeException(ErrorCode.THEME_NOT_FOUND);
        }
        entityManager.remove(theme);
        entityManager.flush();
    }
}
