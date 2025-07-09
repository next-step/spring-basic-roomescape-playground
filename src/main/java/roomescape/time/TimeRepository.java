package roomescape.time;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class TimeRepository {

    private final EntityManager entityManager;

    public TimeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public Time save(Time time) {
        entityManager.persist(time);
        return time;
    }

    public Optional<Time> findById(Long id) {
        Time time = entityManager.find(Time.class, id);
        return Optional.ofNullable(time);
    }

    public List<Time> findAll() {
        return entityManager.createQuery("SELECT t FROM Time t", Time.class)
                .getResultList();
    }

    public List<Time> findAllByIdIn(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery("SELECT t FROM Time t WHERE t.id IN :ids", Time.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Transactional
    public void deleteById(Long id) {
        findById(id).ifPresent(entityManager::remove);
    }
}
