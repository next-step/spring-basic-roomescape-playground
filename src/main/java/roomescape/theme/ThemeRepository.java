package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    public Optional<Theme> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Theme.class, id));
    }

    public List<Theme> findAll() {
        String jpql = "SELECT t FROM Theme t WHERE t.deleted = false";
        TypedQuery<Theme> query = entityManager.createQuery(jpql, Theme.class);
        return query.getResultList();
    }

    public void deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        if (theme != null) {
            theme.setDeleted(true);
            entityManager.merge(theme);
        }
    }
}
