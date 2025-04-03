package roomescape.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByDateAndThemeId(LocalDate date, long themeId);

    List<Reservation> findAllByMemberId(Long id);

    boolean existsByDateAndTimeAndTheme(LocalDate date, Time time, Theme theme);

    boolean existsByMemberIdAndDateAndTimeAndTheme(Long memberId, LocalDate date, Time time, Theme theme);
}
