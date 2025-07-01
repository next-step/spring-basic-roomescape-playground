package roomescape.theme;

import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeRepository {
    private final EntityManager em;

    public ThemeRepository(EntityManager em) {
        this.em = em;
    }

    public List<Theme> findAll() {
        String jpql = "SELECT t FROM Theme t WHERE t.deleted = false";
        return em.createQuery(jpql, Theme.class).getResultList();
    }

    public Theme save(Theme theme) {
        em.persist(theme);
        return theme;
    }

    public void deleteById(Long id) {
        String jpql = "UPDATE Theme t SET t.deleted = true WHERE t.id = :id";
        em.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
    }
}
