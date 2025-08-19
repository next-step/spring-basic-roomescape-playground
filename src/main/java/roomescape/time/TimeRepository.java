package roomescape.time;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.reservation.Reservation;

public interface TimeRepository extends JpaRepository<Time, Long> {

}
