package roomescape.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.model.Member;
import roomescape.model.Time;

import java.util.List;

@Repository
public class TimeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Time> findAll() {
        String jpql = "SELECT t FROM Time t WHERE t.deleted = false";
        TypedQuery<Time> query = entityManager.createQuery(jpql, Time.class);
        return query.getResultList();
    }

    public Optional<Time> findById(Long id) {
        Time time = entityManager.find(Time.class, id);
        return Optional.ofNullable(time);
    }

    public Time save(Time time) {
        entityManager.persist(time);
        return time;
    }

    public void deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time != null) {
            time.delete();
        }
    }
}
