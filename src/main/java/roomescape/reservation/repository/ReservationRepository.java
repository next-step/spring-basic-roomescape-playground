package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import roomescape.reservation.entity.Reservation;
import roomescape.theme.entity.Theme;
import roomescape.time.entity.Time;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Override
    @Query("SELECT r FROM reservation r " +
            "JOIN FETCH r.member " +
            "JOIN FETCH r.time " +
            "JOIN FETCH r.theme")
    List<Reservation> findAll();

    @Query("SELECT r FROM reservation r " +
            "JOIN FETCH r.time " +
            "JOIN FETCH r.theme " +
            "WHERE r.member.id = :memberId")
    List<Reservation> findAllByMemberId(@Param("memberId") Long memberId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    boolean existsByDateAndTimeAndTheme(
            @Param("date") LocalDate date,
            @Param("time") Time time,
            @Param("theme") Theme theme
    );
}
