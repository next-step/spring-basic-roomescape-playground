package roomescape.time;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public class TimeDao {

    private final EntityManager em;

    public TimeDao(EntityManager em) {
        this.em = em;
    }

    public List<Time> findAll() {
        return em.createQuery("SELECT t FROM Time t WHERE t.deleted = false", Time.class)
                .getResultList();
    }

    @Transactional
    public Time save(Time time) {
        em.persist(time);
        return time;
    }

    @Transactional
    public void deleteById(Long id) {
        Time time = em.find(Time.class, id);
        if (time != null) {
            time.setDeleted(true);
        }
    }}
