package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import roomescape.member.model.Member;
import roomescape.reservation.model.Inventory;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationStatus;

import java.util.List;
import java.util.Optional;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByMember(Member member);

    boolean existsByInventoryAndStatus(Inventory inventory, ReservationStatus status);

    @Query("""
        SELECT count(r)
        FROM Reservation r
        WHERE r.inventory.id = :inventoryId
        AND r.status = :status
        AND r.id < :reservationId
    """)
    Long countEarlierReservations(
            Long inventoryId,
            ReservationStatus status,
            Long reservationId
    );

    Optional<Reservation> findFirstByInventoryAndStatus(Inventory inventory, ReservationStatus status);

    @Query("""
        SELECT r
        FROM Reservation r
        JOIN FETCH r.inventory i
        JOIN FETCH i.theme
        JOIN FETCH i.date
        JOIN FETCH i.time
        WHERE r.member.id = :memberId
    """)
    List<Reservation> findDetailedReservations(Long memberId);
}
