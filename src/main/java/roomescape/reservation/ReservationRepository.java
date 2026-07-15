package roomescape.reservation;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {
    private final EntityManager entityManager;

    public ReservationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Reservation save(Reservation reservation) {
        entityManager.persist(reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Reservation.class, id));
    }

    public List<Reservation> findAll() {
        return entityManager.createQuery(
                "SELECT r FROM Reservation r",
                Reservation.class
        ).getResultList();
    }

    public void deleteById(Long id) {
        Reservation reservation = entityManager.find(Reservation.class, id);

        if (reservation != null) {
            entityManager.remove(reservation);
        }
    }
}

