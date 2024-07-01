package roomescape.reservation;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Reservation save(ReservationRequest request);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);
}
