package roomescape.reservation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.member.Member;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long theme_id);

    List<Reservation> findByMember(Member member);
}
