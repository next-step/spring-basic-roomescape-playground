package roomescape.time;

import jakarta.persistence.EntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class TimeRepository {

    private final EntityManager em;

    public TimeRepository(EntityManager em) {
        this.em = em;
    }

    public List<Time> findAll() {
        String jpql = "SELECT t FROM Time t WHERE t.deleted = false";
        return em.createQuery(jpql, Time.class).getResultList();
    }

    public Time save(Time time) {
        em.persist(time);
        return time;
    }

    public void deleteById(Long id) {
        String jpql = "UPDATE Time t SET deleted = true WHERE t.id = :id";
        em.createQuery(jpql)
                .setParameter("id", id)
                .executeUpdate();
    }

}
