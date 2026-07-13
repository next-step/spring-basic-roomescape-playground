package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Theme> findAll() {
        return entityManager.createQuery(
                        "select t from Theme t where t.deleted = false",
                        Theme.class
                )
                .getResultList();
    }

    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    public boolean deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        if (theme == null || theme.deleted()) {
            return false;
        }
        theme.delete();
        return true;
    }

    public boolean existsById(Long id) {
        Long count = entityManager.createQuery(
                        "select count(t) from Theme t where t.id = :id and t.deleted = false",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
