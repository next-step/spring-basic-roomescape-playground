package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WaitingRepository extends JpaRepository<Waiting, Long> {

    boolean existsByMember_IdAndDateAndTime_IdAndTheme_Id(Long memberId, String date, Long timeId, Long themeId);

    default boolean existsForMemberOnSlot(Long memberId, String date, Long timeId, Long themeId) {
        return existsByMember_IdAndDateAndTime_IdAndTheme_Id(memberId, date, timeId, themeId);
    }

    List<Waiting> findByMember_IdOrderByIdAsc(Long memberId);

    long countByTheme_IdAndDateAndTime_IdAndIdLessThan(Long themeId, String date, Long timeId, Long idLowerThan);
}


