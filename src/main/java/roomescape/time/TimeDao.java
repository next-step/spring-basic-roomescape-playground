package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Time> findById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time == null || time.deleted()) {
            return Optional.empty();
        }
        return Optional.of(time);
    }

    public boolean deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time == null || time.deleted()) {
            return false;
        }
        time.delete();
        return true;
    }

}
