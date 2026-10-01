package roomescape.reservation;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN FETCH r.time
        WHERE r.date = :date
          AND r.theme.id = :themeId
        """)
    List<Reservation> findByDateAndThemeId(
            @Param("date") String date,
            @Param("themeId") Long themeId
    );

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN FETCH r.theme
        JOIN FETCH r.time
        WHERE r.member.id = :memberId
        """)
    List<Reservation> findAllByMemberId(
            @Param("memberId") Long memberId
    );

    @Query("""
        SELECT r
        FROM Reservation r
        LEFT JOIN FETCH r.member
        JOIN FETCH r.theme
        JOIN FETCH r.time
        """)
    List<Reservation> findAllWithDetails();

    boolean existsByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM Reservation r
        WHERE r.date = :date
          AND r.time.id = :timeId
          AND r.theme.id = :themeId
        """)
    Optional<Reservation> findBySlotForUpdate(
            @Param("date") String date,
            @Param("timeId") Long timeId,
            @Param("themeId") Long themeId
    );
}
