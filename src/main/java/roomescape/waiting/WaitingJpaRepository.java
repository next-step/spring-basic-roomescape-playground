package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingJpaRepository extends JpaRepository<Waiting, Long> {

    void deleteByMemberIdAndId(MemberId memberId, Long id);

    boolean existsByMemberIdAndThemeIdAndTimeId(MemberId memberId, ThemeId themeId, TimeId timeId);
}
