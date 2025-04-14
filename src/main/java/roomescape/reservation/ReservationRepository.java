package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme JOIN FETCH r.time JOIN FETCH r.member")
    List<Reservation> findAllWithThemeAndTime();

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.theme t JOIN FETCH r.time WHERE r.member.id = :memberId")
    List<Reservation> findByMemberId(Long memberId);

    boolean existsByMemberAndThemeAndDateAndTime(Member member, Theme theme, String date, Time time);
}
