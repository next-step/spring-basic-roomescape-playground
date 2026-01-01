package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TimeRepository {

    @PersistenceContext
    private EntityManager em;

    public TimeRepository(EntityManager em) {
        this.em = em;
    }

    public long count() {
        return em.createQuery("SELECT COUNT(t) FROM Time t", Long.class)
                .getSingleResult();
    }

    public List<Time> findAll() {
        return em.createQuery("SELECT t FROM Time t WHERE t.deleted = false", Time.class)
                .getResultList();
    }

    public Time save(Time time) {
        em.persist(time);
        return time;
    }

    public Optional<Time> findById(Long id) {
        Time time = em.find(Time.class, id);
        return Optional.of(time);
    }
}
