package roomescape.waiting;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.theme.Theme;
import roomescape.time.Time;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    List<Waiting> findByDateAndTimeAndTheme(String date, Time time, Theme theme);

    List<Waiting> findByDateAndTimeAndThemeAndOrderAfter(String date, Time time, Theme theme, Long orderAfter);

    Long countByDateAndTimeAndTheme(String date, Time time, Theme theme);

    List<Waiting> findByMemberId(Long memberId);

    void deleteById(Long id);
}
