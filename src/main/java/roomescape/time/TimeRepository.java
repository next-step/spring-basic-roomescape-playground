package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

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
        entityManager.flush();
        return time;
    }

    public void deleteById(Long id) {
        Time time = entityManager.find(Time.class, id);
        if (time == null) {
            throw new RoomEscapeException(ErrorCode.TIME_NOT_FOUND);
        }

        boolean isReferenceInReservation = entityManager.createQuery(
                        "SELECT COUNT(r) > 0 FROM Reservation r WHERE r.time.id = :timeId", Boolean.class
                ).setParameter("timeId", id)
                .getSingleResult();

        boolean isReferenceInWaiting = entityManager.createQuery(
                        "SELECT COUNT(w) > 0 FROM Waiting w WHERE w.time.id = :timeId", Boolean.class
                ).setParameter("timeId", id)
                .getSingleResult();

        if (isReferenceInReservation || isReferenceInWaiting) {
            throw new RoomEscapeException(ErrorCode.DELETE_CONFLICT, "시간이 다른 자원에서 사용중입니다.");
        }

        time.softDelete();
        entityManager.flush();
    }
}
