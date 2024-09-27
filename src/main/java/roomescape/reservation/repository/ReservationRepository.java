package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(String date, Long theme_id);

    List<Reservation> findByMemberId(Long memberId);

    @Query("SELECT r FROM Reservation r " +
        "WHERE r.date = :date " +
        "AND r.theme = :theme " +
        "AND r.time = :time " +
        "AND r.member = :member")
    Optional<Reservation> findExistReservation(
        @Param("date") String date,
        @Param("theme") Theme theme,
        @Param("time") Time time,
        @Param("member") Member member
    );
}
