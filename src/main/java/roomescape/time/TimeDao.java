package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimeDao {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Time> findAll() {
        return entityManager.createQuery(
                        "select t from Time t where t.deleted = false",
                        Time.class
                )
                .getResultList();
    }

    public Time save(Time time) {
        entityManager.persist(time);
        return time;
    }

    public boolean deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time == null || time.deleted()) {
            return false;
        }
        time.delete();
        return true;
    }

    public boolean existsById(Long id) {
        Long count = entityManager.createQuery(
                        "select count(t) from Time t where t.id = :id and t.deleted = false",
                        Long.class
                )
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
