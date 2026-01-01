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

    public Time save(Time time) {
        if (time.getId() == null) {
            em.persist(time);
            return time;
        }
        return em.merge(time);
    }

    public Optional<Time> findById(Long id) {
        return Optional.ofNullable(em.find(Time.class, id));
    }

    public List<Time> findAll() {
        return em.createQuery("SELECT t FROM Time t.deleted = false", Time.class).getResultList();
    }

    public void deleteById(Long id) {
        Time find = em.find(Time.class, id);
        if (find != null) {
            find.delete();
        }
    }
}
