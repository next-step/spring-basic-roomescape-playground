package roomescape.reservation.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import roomescape.auth.dto.response.LoginMember;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationResponse;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb")
@Transactional
public class ReservationServiceTest {

	@Autowired
	private ReservationService reservationService;

	private ObjectMapper objectMapper;
	private ReservationRequest reservationRequest;
	private LoginMember loginMember;

	@BeforeEach
	void setUp() throws JsonProcessingException {
		objectMapper = new ObjectMapper();
		String json = "{ \"name\": \"어드민\", \"date\": \"2024-03-01\", \"theme\": 1, \"time\": 1 }";
		reservationRequest = objectMapper.readValue(json, ReservationRequest.class);
		loginMember = new LoginMember(1L, "어드민", "admin@email.com", "ADMIN");
	}

	@Test
	void 예약을_생성할_수_있다() {
		ReservationResponse reservationResponse = reservationService.save(reservationRequest, loginMember);

		assertThat(reservationResponse).isNotNull();
		assertThat(reservationResponse.getId()).isNotNull();
		assertThat(reservationResponse.getName()).isEqualTo("어드민");
		assertThat(reservationResponse.getDate()).isEqualTo("2024-03-01");
		assertThat(reservationResponse.getTheme()).isEqualTo("테마1");
	}

	@Test
	void Request에_이름이_없는_경우_예약을_생성할_수_있다() {
		reservationRequest.setName(null);
		assertThat(reservationRequest.getName()).isNull();

		ReservationResponse reservationResponse = reservationService.save(reservationRequest, loginMember);

		assertThat(reservationResponse).isNotNull();
		assertThat(reservationResponse.getId()).isNotNull();
		assertThat(reservationResponse.getName()).isEqualTo("어드민");
		assertThat(reservationResponse.getDate()).isEqualTo("2024-03-01");
		assertThat(reservationResponse.getTheme()).isEqualTo("테마1");
	}
}
