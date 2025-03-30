package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndTheme(String date, Theme theme);

    List<Reservation> findAllByMemberId(Long memberId);

    Reservation findByDateAndTimeIdAndThemeId(String date, Long time, Long theme);

    Optional<Reservation> findById(Long id);

    boolean existsByDateAndTimeIdAndThemeId(String date, Long id1, Long id2);
}
