package roomescape.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByMemberId(Long memberId);

	List<Reservation> findByDateAndThemeId(String date, Long themeId);

}
