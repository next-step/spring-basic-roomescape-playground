package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {

    private final EntityManager entityManager;

    public ThemeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    public Optional<Theme> findById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        return Optional.ofNullable(theme);
    }

    public List<Theme> findAll() {
        return entityManager.createQuery("SELECT t FROM Theme t", Theme.class)
                .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
