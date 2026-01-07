package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TimeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Time save(Time time) {
        entityManager.persist(time);
        return time;
    }

    public Optional<Time> findById(Long id) {
        Time time = entityManager.find(Time.class, id);
        return Optional.ofNullable(time);
    }

    public List<Time> findAll() {
        String jpql = "SELECT t FROM Time t WHERE t.deleted = false";
        TypedQuery<Time> query = entityManager.createQuery(jpql, Time.class);
        return query.getResultList();
    }

    public void deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time != null) {
            time.setDeleted(true);
            entityManager.merge(time);
        }
    }
}
