package roomescape.theme.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.theme.entity.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaThemeRepository implements ThemeRepository {

    private final EntityManager entityManager;

    public JpaThemeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Theme save(Theme theme) {
        entityManager.persist(theme);
        return theme;
    }

    @Override
    public List<Theme> findAll() {
        String jpql = "SELECT t FROM theme AS t";
        return entityManager.createQuery(jpql, Theme.class)
                .getResultList();
    }

    @Override
    public Optional<Theme> findById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        return Optional.ofNullable(theme);
    }

    @Override
    public void deleteById(Long id) {
        Theme theme = entityManager.find(Theme.class, id);
        entityManager.remove(theme);
    }
}
