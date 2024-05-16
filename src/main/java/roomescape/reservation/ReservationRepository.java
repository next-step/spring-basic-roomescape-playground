package roomescape.reservation;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findAll();
    List<Reservation> findByDateAndThemeId(String date, Long themeId);
    List<Reservation> findByMemberId(Long memberId);
}
