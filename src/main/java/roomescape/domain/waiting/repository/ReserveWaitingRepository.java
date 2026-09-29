package roomescape.domain.waiting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import roomescape.domain.member.entity.Member;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;
import roomescape.domain.waiting.entity.ReserveWaiting;
import roomescape.domain.waiting.service.result.WaitingWithRank;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReserveWaitingRepository extends ListCrudRepository<ReserveWaiting, Long> {

    /**
     * 한 유저는 같은 날짜, 시각, 테마에 대한 예약 대기를 1개만 생성할 수 있는 복합 유니크 제약 조건을 위배하지 않는지 검사하는 메소드입니다.
     * @return true를 반환할 시 uk_reserve_waiting_member_date_time_theme을 위배.
     */
    boolean existsByMemberAndDateAndTimeAndTheme(Member member, LocalDate date, Time time, Theme theme);

    /**
     * 같은 날짜, 시각, 테마와 연관된 예약 대기 레코드 행수를 조회하는 메소드입니다.
     * @param reserveWaitingId 생성한 예약 대기 건의 ID를 전달해주세요.<br>GenerationType.IDENTITY 등 ID가 순차 증가할 때에만 유효하며, 이 ID보다 값이 작은 레코드 행수를 계산합니다.
     * @return 같은 날짜, 시각, 테마에 대한 예약 대기를 전달받은 reserve_waiting.id보다 작은 레코드 행수를 반환
     */
    Long countByDateAndTimeAndThemeAndIdLessThan(LocalDate date, Time time, Theme theme, Long reserveWaitingId);

    /**
     * 특정 Member의 모든 예약 대기를 조회합니다.
     * @return memberId에 해당하는 모든 예약 대기 레코드를 조회하며,<br>날짜, 시각, 테마가 완전히 일치하며 memberId에 해당하는 레코드보다 먼저, 해당하는 레코드의 개수를 셉니다. GenerationType.IDENTITY 등 ID 값이 순차 증가하는 경우에만 유효합니다.
     */
    @SuppressWarnings("JpaQlInspection")
    @Query("""
select new roomescape.domain.waiting.service.result.WaitingWithRank(
    rw,
    (select count(rw2)
    from ReserveWaiting as rw2
    where rw2.theme = rw.theme
        and rw2.date = rw.date
        and rw2.time = rw.time
        and rw2.id <= rw.id))
    from ReserveWaiting as rw
    where rw.member.id = :memberId
""")
    List<WaitingWithRank> findWaitingWithRankByMemberId(
            @Param("memberId") Long memberId
    );

    Optional<ReserveWaiting> findByIdAndMemberId(Long reserveWaitingId, Long memberId);
}
