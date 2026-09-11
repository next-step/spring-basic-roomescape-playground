package roomescape.reservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class ReservationDaoTest {
    private ReservationDao reservationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        reservationDao = new ReservationDao(jdbcTemplate);
    }

    @Test
    void 전달받은_회원_이름으로_예약을_저장한다() {
        // given
        ReservationRequest request = new ObjectMapper().convertValue(
                Map.of("name", "요청이름", "date", "2027-08-15", "theme", 1L, "time", 1L), ReservationRequest.class);

        // when
        Reservation reservation = reservationDao.save(request, "브라운");

        // then
        assertThat(reservation.getName()).isEqualTo("브라운");
        assertThat(jdbcTemplate.queryForObject("select name from reservation where id = ?", String.class, reservation.getId()))
                .isEqualTo("브라운");
    }
}
