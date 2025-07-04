package roomescape.theme;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class ThemeDao {

    private final EntityManager em;

    public ThemeDao(EntityManager em) {
        this.em = em;
    }

    public List<Theme> findAll() {
        return em.createQuery("SELECT t FROM Theme t WHERE t.deleted = false", Theme.class)
                .getResultList();
    }

    @Transactional
    public Theme save(Theme theme) {
        em.persist(theme);
        return theme;
    }

    @Transactional
    public void deleteById(Long id) {
        Theme theme = em.find(Theme.class, id);
        if (theme != null) {
            theme.setDeleted(true);
        }
    }}
