package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeRepository {
    @PersistenceContext
    private EntityManager em;

    public Theme save(Theme theme) {
        if (theme.getId() == null) {
            em.persist(theme);
            return theme;
        }
        return em.merge(theme);
    }

    public Optional<Theme> findById(Long id) {
        return Optional.ofNullable(em.find(Theme.class, id));
    }

    public List<Theme> findAll() {
        return em.createQuery("SELECT t FROM Theme t.deleted false", Theme.class).getResultList();
    }

    public void deleteById(Long id) {
        Theme find = em.find(Theme.class, id);
        if (find != null) {
            find.delete();
        }
    }
}
