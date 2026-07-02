package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingReservationRepository extends JpaRepository<WaitingReservation, Long> {
}
