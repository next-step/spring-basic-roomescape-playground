package roomescape.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> findByMemberId(Long memberId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Reservation r " +
           "WHERE r.date = :date AND r.time.id = :timeId AND r.theme.id = :themeId")
    boolean existsByDateAndTimeAndTheme(@Param("date") String date, @Param("timeId") Long timeId, @Param("themeId") Long themeId);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM Reservation r " +
           "WHERE r.member.id = :memberId AND r.date = :date " +
           "AND r.time.id = :timeId AND r.theme.id = :themeId")
    boolean existsByMemberAndDateAndTimeAndTheme(@Param("memberId") Long memberId, @Param("date") String date, @Param("timeId") Long timeId, @Param("themeId") Long themeId);
}
