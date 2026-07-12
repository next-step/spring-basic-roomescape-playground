package roomescape.time.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import roomescape.time.entity.Time;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaTimeRepository implements TimeRepository {

    private final EntityManager entityManager;

    public JpaTimeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Time> findAll() {
        String jpql = "SELECT t FROM time AS t";
        return entityManager.createQuery(jpql, Time.class)
                .getResultList();
    }

    @Override
    public Optional<Time> findById(Long id) {
        Time time = entityManager.find(Time.class, id);
        return Optional.ofNullable(time);
    }

    @Override
    public Time save(Time time) {
        entityManager.persist(time);
        return time;
    }

    @Override
    public void deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        entityManager.remove(time);
    }
}
