package roomescape.reservation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import roomescape.theme.Theme;
import roomescape.time.Time;

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
		Time time = Time.ofDeletedFalse("10:00");
		Theme theme = Theme.ofDeletedFalse("theme", "description");
		Reservation reservation = new Reservation("name", "2023-10-10", time, theme);

		entityManager.persist(time);
		entityManager.persist(theme);
		entityManager.persist(reservation);
		entityManager.flush();
		entityManager.clear();

		// when
		List<Reservation> reservations = reservationRepository.findAllWithThemeAndTime();
		Reservation foundReservation = reservations.stream().filter(savedReservation -> savedReservation.getId().equals(reservation.getId()))
			.findFirst()
			.orElse(null);

		// then
		assertAll(
			() -> assertThat(foundReservation).isNotNull(),
			() -> assertThat(foundReservation.getName()).isEqualTo(reservation.getName()),
			() -> assertThat(foundReservation.getDate()).isEqualTo(reservation.getDate()),
			() -> assertThat(foundReservation.getTime().getId()).isEqualTo(time.getId()),
			() -> assertThat(foundReservation.getTheme().getId()).isEqualTo(theme.getId())
		);
	}
}
