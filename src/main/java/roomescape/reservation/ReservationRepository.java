package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.member.Member;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findAllByMember(Member member);
}
