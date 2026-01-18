package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndTheme_Id(String date, Long themeId);

    boolean existsByDateAndTime_IdAndTheme_Id(String date, Long timeId, Long themeId);

    @Query("""
        SELECT r FROM Reservation r
        JOIN FETCH r.time
        JOIN FETCH r.theme
        LEFT JOIN FETCH r.member
    """)
    List<Reservation> findAllWithRelations();

    @Query("""
        SELECT r FROM Reservation r
        JOIN FETCH r.time
        JOIN FETCH r.theme
        JOIN FETCH r.member
        WHERE r.member.id = :memberId
    """)
    List<Reservation> findMineWithRelations(Long memberId);
}
