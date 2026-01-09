package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TimeRepository{

    @PersistenceContext
    private EntityManager em;

    public Time save(Time time) {
        em.persist(time);
        return time;
    }

    public Optional<Time> findById(Long id) {
        Time time = em.find(Time.class, id);
        return Optional.ofNullable(time);
    }

    public List<Time> findAll() {
        return em.createQuery("SELECT t FROM Time t", Time.class)
                .getResultList();
    }

    public void delete(Long id) {
        Time time = em.find(Time.class, id);
        if (time != null) {
            em.remove(time);
        }
    }
}
