package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ThemeRepository {

    @PersistenceContext
    private EntityManager em;

    public Theme save(Theme theme) {
        em.persist(theme);
        return theme;
    }

    public Theme findById(Long id) {
        return em.find(Theme.class, id);
    }

    public List<Theme> findAll() {
        return em.createQuery("SELECT t FROM Theme t", Theme.class)
                .getResultList();
    }

    public void delete(Long id) {
        Theme theme = em.find(Theme.class, id);
        if (theme != null) {
            em.remove(theme);
        }
    }
}
