package roomescape.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.MemberDao;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({ReservationService.class, ReservationDao.class, MemberDao.class})
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 이름이_없으면_로그인_회원으로_예약을_저장하고_이름을_반환한다() {
        // given
        ReservationRequest request = createReservationRequest(null);
        Long memberId = jdbcTemplate.queryForObject("select id from member where email = ?", Long.class, "brown@email.com");

        // when
        ReservationResponse response = reservationService.save(request, memberId);

        // then
        assertThat(response.getName()).isEqualTo("브라운");
        assertThat(jdbcTemplate.queryForObject("select name from reservation where id = ?", String.class, response.getId()))
                .isEqualTo("브라운");
    }

    @Test
    void 이름이_있으면_로그인_회원보다_지정한_회원을_우선한다() {
        // given
        ReservationRequest request = createReservationRequest("브라운");
        Long memberId = jdbcTemplate.queryForObject("select id from member where email = ?", Long.class, "admin@email.com");

        // when
        ReservationResponse response = reservationService.save(request, memberId);

        // then
        assertThat(response.getName()).isEqualTo("브라운");
        assertThat(jdbcTemplate.queryForObject("select name from reservation where id = ?", String.class, response.getId()))
                .isEqualTo("브라운");
    }

    private ReservationRequest createReservationRequest(String name) {
        Map<String, Object> values = new HashMap<>(Map.of("date", "2027-08-15", "theme", 1L, "time", 1L));
        values.put("name", name);
        return new ObjectMapper().convertValue(values, ReservationRequest.class);
    }
}
