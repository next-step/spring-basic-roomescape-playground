package roomescape.reservation;

import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    @Override
    @EntityGraph(attributePaths = {"theme", "time", "member"})
    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"theme", "time", "member"})
    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    @EntityGraph(attributePaths = {"theme", "time", "member"})
    List<Reservation> findByMember_id(Long memberId);
}
