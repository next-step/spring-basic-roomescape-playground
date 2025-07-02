package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
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
        entityManager.remove(theme);
    }
}
