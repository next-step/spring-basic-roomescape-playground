package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import roomescape.member.Member;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndTheme_Id(LocalDate date, long themeId);

    @Query("select r from Reservation r left join fetch r.reservationTime")
    List<Reservation> findAllWithReservationTime();

    @Query("SELECT r FROM Reservation r join fetch r.member m WHERE m.id = :memberId")
    List<Reservation> findAllByMemberId(long memberId);

    Optional<Reservation> findByDateAndReservationTime_IdAndTheme_Id(LocalDate date, long timeId,
                                                                     long themeId);

    boolean existsByDateAndTheme_IdAndReservationTime_Id(LocalDate date, long themeId, long reservationId);

//    @EntityGraph(value = "Reservation.forStudies", type = EntityGraph.EntityGraphType.FETCH)
//    @Query("select r from Reservation r")
//    List<Reservation> findAllWithForStudyByEntityGraph();

    @Query("SELECT r FROM Reservation r join fetch r.member m WHERE m = :member")
    List<Reservation> findAllForTest(Member member);
}
