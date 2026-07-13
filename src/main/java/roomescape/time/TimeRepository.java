package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TimeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Time> findAll() {

        return entityManager.createQuery(
                "SELECT t FROM Time t WHERE t.deleted = false",
                Time.class
        ).getResultList();
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

    public Optional<Time> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Time.class, id));
    }
}
