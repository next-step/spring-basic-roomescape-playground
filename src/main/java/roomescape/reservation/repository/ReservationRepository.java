package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndTheme(String date, Theme theme);

    List<Reservation> findAllByMemberId(Long memberId);

    Optional<Reservation> findByDateAndTimeIdAndThemeId(String date, Long time, Long theme);

    Optional<Reservation> findById(Long id);

    boolean existsByDateAndTimeIdAndThemeId(String date, Long timeId, Long ThemeId);
}
