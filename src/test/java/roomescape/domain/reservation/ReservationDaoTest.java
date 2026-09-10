package roomescape.domain.reservation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({ReservationDao.class})
public class ReservationDaoTest {

    private final String name = "Alice";
    private final LocalDate date = LocalDate.now().plusDays(1);

    @Autowired
    private ReservationDao reservationDao;

    @Test
    void save를_호출하면_ID가_있는_객체를_반환한다() {
        // when
        Reservation savedReservation = reservationDao.save(name, date, 1L, 1L);

        // then
        assertThat(savedReservation.getId()).isNotNull();
        assertThat(savedReservation.getName()).isEqualTo(name);
        assertThat(savedReservation.getDate()).isEqualTo(date);
        assertThat(savedReservation.getTheme().getId()).isEqualTo(1L);
        assertThat(savedReservation.getTime().getId()).isEqualTo(1L);
    }

    @Test
    void findAll을_호출하면_저장된_모든_예약을_반환한다() {
        // schema.sql 시드 예약 3건
        assertThat(reservationDao.findAll()).hasSize(3);

        // when
        reservationDao.save(name, date, 1L, 1L);

        // then
        assertThat(reservationDao.findAll()).hasSize(4);
    }

    @Test
    void deleteById를_호출하면_해당_예약이_삭제된다() {
        // when
        reservationDao.deleteById(1L);

        // then
        assertThat(reservationDao.findAll()).hasSize(2);
    }

    @Test
    void findByDateAndThemeId를_호출하면_날짜와_테마가_일치하는_예약만_반환한다() {
        // schema.sql 시드 예약 (2024-03-01, theme_id = 1)
        List<Reservation> reservations = reservationDao.findByDateAndThemeId(LocalDate.of(2024, 3, 1), 1L);

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations.get(0).getName()).isEqualTo("어드민");
    }
}
