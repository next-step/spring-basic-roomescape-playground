package roomescape.reservation.waiting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import roomescape.theme.Theme;
import roomescape.time.Time;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

	List<Waiting> findByName(String name);

	List<Waiting> findAllByDateAndTimeAndThemeOrderByDateAscTimeAsc(String date, Time time, Theme theme);
}
