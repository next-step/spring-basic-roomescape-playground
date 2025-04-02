package roomescape.reservation;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ReservationRepositoryTest {

	@Autowired
	private ReservationRepository reservationRepository;

	@DisplayName("")
	@Test
	void testMethodNameHere() {
		//given
		List<Reservation> reservations = reservationRepository.findAllWithThemeAndTime();
		// when
		// then
	}
}
