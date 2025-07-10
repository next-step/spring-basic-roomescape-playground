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

        boolean isReferenceInReservation = entityManager.createQuery(
                        "SELECT COUNT(r) > 0 FROM Reservation r WHERE r.time.id = :themeId", Boolean.class
                ).setParameter("themeId", id)
                .getSingleResult();

        boolean isReferenceInWaiting = entityManager.createQuery(
                        "SELECT COUNT(w) > 0 FROM Waiting w WHERE w.time.id = :themeId", Boolean.class
                ).setParameter("themeId", id)
                .getSingleResult();

        if (isReferenceInReservation || isReferenceInWaiting) {
            throw new RoomEscapeException(ErrorCode.DELETE_CONFLICT, "테마가 다른 자원에서 사용중입니다.");
        }

        theme.softDelete();
        entityManager.flush();
    }
}
