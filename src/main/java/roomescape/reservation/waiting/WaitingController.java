package roomescape.reservation.waiting;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import roomescape.auth.LoginMember;
import roomescape.reservation.ReservationRequest;
import roomescape.reservation.ReservationService;

@RestController
public class WaitingController {

	private final ReservationService reservationService;

	public WaitingController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping("/waitings")
	public ResponseEntity createWait(@RequestBody ReservationRequest reservationRequest, LoginMember member) {
		if (reservationRequest.getDate() == null
			|| reservationRequest.getTheme() == null
			|| reservationRequest.getTime() == null) {
			return ResponseEntity.badRequest().build();
		}
		WaitingResponse waiting = reservationService.wait(reservationRequest, member);

		return ResponseEntity.created(URI.create("/waitings/" + waiting.waitingId())).body(waiting);
	}
}
