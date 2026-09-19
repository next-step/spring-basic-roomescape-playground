package roomescape.waiting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.waiting.domain.Waiting;

import java.time.LocalDate;
import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    boolean existsByMember_IdAndDateAndTheme_IdAndTime_Id(
            Long memberId,
            LocalDate date,
            Long themeId,
            Long timeId
    );

    List<Waiting> findByMember_IdOrderByIdAsc(Long memberId);
}
