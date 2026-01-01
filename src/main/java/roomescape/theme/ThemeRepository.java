package roomescape.theme;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {
    private EntityManager em;

    public long count() {
        return em.createQuery("SELECT COUNT(t) FROM Theme t", Long.class)
                .getSingleResult();
    }

    public ThemeRepository(EntityManager em) {
        this.em = em;
    }

    public List<Theme> findAll() {
        return em.createQuery("SELECT t FROM Theme t WHERE t.deleted = false", Theme.class).getResultList();
    }

    public Theme save(Theme theme) {
        em.persist(theme);
        return theme;
    }

    public void deleteById(Long id) {
        Theme theme = em.find(Theme.class, id);
        if (theme != null) {
            theme.setDeleted(true);
        }
    }

    public Optional<Theme> findById(Long id) {
        Theme theme = em.find(Theme.class, id);
        return Optional.of(theme);
    }
}

