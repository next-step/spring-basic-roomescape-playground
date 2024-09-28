package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import roomescape.member.Member;
import roomescape.reservation.model.Reservation;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findByMemberAndThemeAndTimeAndDate(Member member, Theme theme, Time time, String date);

    List<Reservation> findByDateAndThemeId(String date, Long theme_id);

    List<Reservation> findByMember(Member member);
}
