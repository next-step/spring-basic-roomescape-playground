package roomescape.reservation;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"theme", "time"})
    List<Reservation> findByMember_Id(Long memberId);

    List<Reservation> findByDateAndTheme_Id(String date, Long themeId);

    boolean existsByMember_IdAndDateAndTime_IdAndTheme_Id(Long memberId, String date, Long timeId, Long themeId);

    default boolean existsForMemberOnSlot(Long memberId, String date, Long timeId, Long themeId) {
        return existsByMember_IdAndDateAndTime_IdAndTheme_Id(memberId, date, timeId, themeId);
    }
}


