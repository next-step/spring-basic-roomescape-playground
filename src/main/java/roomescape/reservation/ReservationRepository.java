package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndTheme_Id(LocalDate date, long themeId);

    @Query("select r from Reservation r join fetch r.reservationTime")
    List<Reservation> findAllWithReservationTime();


    /**
     * 아래는 학습을 위해 만든 메서드입니다.
     * fetch join, entityGraph 동작 확인 용
     */
    @Query("select r from Reservation r join fetch r.forStudies")
    List<Reservation> findAllWithForStudy();

    @EntityGraph(value = "Reservation.forStudies", type = EntityGraph.EntityGraphType.FETCH)
    @Query("select r from Reservation r")
    List<Reservation> findAllWithForStudyWithEntityGraph();
}
