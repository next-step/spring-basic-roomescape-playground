package roomescape.waiting;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import roomescape.reservation.MyReservationResponse;

import java.time.LocalDate;
import java.util.List;

@Repository
public class WaitingRepository {

    private final WaitingJpaRepository waitingJpaRepository;
    private final JdbcTemplate jdbcTemplate;

    public WaitingRepository(WaitingJpaRepository waitingJpaRepository, JdbcTemplate jdbcTemplate) {
        this.waitingJpaRepository = waitingJpaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Waiting waiting) {
        waitingJpaRepository.save(waiting);
    }


    public Long findMyRank(Long memberId, LocalDate date, Long theme, Long time) {
        String sql = """
                SELECT *
                FROM (
                SELECT
                  *,
                  RANK() OVER (PARTITION BY theme_id, time_id, date ORDER BY id) AS rank
                FROM WAITING
                WHERE theme_id = ? AND date = ? AND time_id = ?
                )
                where member_id = ?;
                """;

        WaitingWithRank waitingWithRank = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new WaitingWithRank(
                new Waiting(
                        new MemberId(rs.getLong("member_id")),
                        new ThemeId(rs.getLong("theme_id")),
                        new TimeId(rs.getLong("time_id")),
                        rs.getDate("date").toLocalDate()
                ),
                rs.getLong("rank")
        ), theme, date, time, memberId);

        return waitingWithRank != null ? waitingWithRank.getRank() : 0;
    }

    public List<MyReservationResponse> findAllByMemberId(Long memberId) {
        String sql = """
                SELECT
                  A.id,
                  B.name,
                  A.date,
                  C.time_value,
                  RANK() OVER (PARTITION BY A.theme_id, A.time_id, A.date ORDER BY A.id) AS rank
                FROM WAITING A
                JOIN THEME B ON A.theme_id = B.id
                JOIN TIME C ON A.time_id = C.id
                where A.member_id = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new MyReservationResponse(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("date"),
                rs.getString("time_value"),
                rs.getString("rank") + "번째 예약대기"
        ), memberId);
    }
}
