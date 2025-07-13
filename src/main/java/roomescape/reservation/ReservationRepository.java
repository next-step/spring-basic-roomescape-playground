package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r JOIN FETCH r.member JOIN FETCH r.theme JOIN FETCH r.time")
    List<Reservation> findAllWithDetails();

    @Query("SELECT r FROM Reservation r JOIN FETCH r.member JOIN FETCH r.theme JOIN FETCH r.time WHERE r.id = :id")
    Optional<Reservation> findByIdWithDetails(@Param("id") Long id);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.member JOIN FETCH r.theme JOIN FETCH r.time WHERE r.member.id = :memberId")
    List<Reservation> findWithDetailsByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.time WHERE r.date = :date AND r.theme = :theme")
    List<Reservation> findByDateAndTheme(@Param("date") LocalDate date, @Param("theme") Theme theme);

    boolean existsByThemeAndDateAndTimeAndMember(Theme theme, LocalDate date, Time time, Member member);
}
