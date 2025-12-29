package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TimeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Time save(Time time) {
        entityManager.persist(time);
        return time;
    }

    public List<Time> findAll() {
        String jpql = "SELECT t FROM Time t WHERE t.deleted = false";
        TypedQuery<Time> query = entityManager.createQuery(jpql, Time.class);
        return query.getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time != null) {
            time.setDeleted(true);
            entityManager.merge(time);
        }
    }
}
