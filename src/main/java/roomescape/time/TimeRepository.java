package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TimeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Time> findAll() {
        return entityManager.createQuery("SELECT ti FROM Time ti WHERE ti.deleted = false", Time.class)
                .getResultList();
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
        entityManager.remove(time);
    }
}
