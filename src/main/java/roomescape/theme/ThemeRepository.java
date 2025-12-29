package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ThemeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    public List<Theme> findAll() {
        String jpql = "SELECT t FROM Theme t WHERE t.deleted = false";
        TypedQuery<Theme> query = entityManager.createQuery(jpql, Theme.class);
        return query.getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        if (theme != null) {
            theme.setDeleted(true);
            entityManager.merge(theme);
        }
    }
}
