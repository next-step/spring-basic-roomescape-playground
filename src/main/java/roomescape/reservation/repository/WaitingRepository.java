package roomescape.reservation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.reservation.domain.Waiting;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    List<Waiting> findByMemberId(Long memberId);

    long countByDateAndTimeIdAndThemeIdAndIdLessThan(
            String date,
            Long timeId,
            Long themeId,
            Long id);
}
