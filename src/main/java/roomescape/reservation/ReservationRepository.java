package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.theme th " +
            "JOIN FETCH r.time t")
    @Override
    @NonNull
    List<Reservation> findAll();

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.theme th " +
            "JOIN FETCH r.time t " +
            "WHERE r.member = :member")
    List<Reservation> findByMember(@Param("member") Member member);

    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH r.time t " +
            "WHERE r.date = :date AND r.theme = :theme")
    List<Reservation> findByDateAndTheme(@Param("date") String date,
                                         @Param("theme") Theme theme);

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.date = :date AND r.theme = :theme AND r.time = :time")
    List<Reservation> findByDateAndThemeAndTime(@Param("date") String date,
                                                @Param("theme") Theme theme,
                                                @Param("time") Time time);

}
