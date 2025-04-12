package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import roomescape.member.Member;
import roomescape.member.enums.Role;
import roomescape.theme.Theme;
import roomescape.time.Time;

@ActiveProfiles("test")
@DataJpaTest
class ReservationRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("findAllWithThemeAndTime : 예약 조회")
    @Test
    void given_save_reservation_when_findAll_then_return_list_contain_savedReservation() {
        //given
        Time time = new Time("10:00");
        Theme theme = new  Theme("themeId", "description");
        Member member = new Member("member", "password", "email", Role.ADMIN);
        Reservation reservation = new Reservation(member.getName(), "2023-10-10", time, theme, member);

        entityManager.persist(time);
        entityManager.persist(theme);
        entityManager.persist(member);
        entityManager.persist(reservation);
        entityManager.flush();
        entityManager.clear();

        // when
        List<Reservation> reservations = reservationRepository.findAllWithThemeAndTime();

        // then
        assertAll(
                () -> assertThat(reservations).isNotEmpty(),
                () -> assertThat(reservations.get(0).getMember().getName()).isEqualTo("member"),
                () -> assertThat(reservations.get(0).getDate()).isEqualTo("2023-10-10"),
                () -> assertThat(reservations.get(0).getTheme().getName()).isEqualTo("themeId")
        );

    }
}
