package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import roomescape.model.Theme;

import java.util.List;

@Repository
public class ThemeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Theme> findAll() {
        String jpql = "SELECT t FROM Theme t WHERE t.deleted = false";
        TypedQuery<Theme> query = entityManager.createQuery(jpql, Theme.class);
        return query.getResultList();
    }

    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    public void deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        if (theme != null) {
            theme.delete();
        }
    }
}
