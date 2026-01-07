package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMemberDto;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final MyReservationService myReservationService;

    public ReservationController(ReservationService reservationService, MyReservationService myReservationService) {
        this.reservationService = reservationService;
        this.myReservationService = myReservationService;
    }

	@GetMapping("/reservations")
	public List<ReservationResponseDto> list() {
        return reservationService.findAll();
    }

    @GetMapping("/admin/reservations")
	public List<ReservationResponseDto> adminList() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
	public ResponseEntity create(@RequestBody @Valid ReservationRequestDto reservationRequest, LoginMemberDto member) {

        String effectiveName = reservationRequest.getName() != null && !reservationRequest.getName().isBlank()
                ? reservationRequest.getName()
                : (member != null ? member.getName() : null);
        if (effectiveName == null || effectiveName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
		ReservationRequestDto requestWithName = new ReservationRequestDto(
                effectiveName,
                reservationRequest.getDate(),
                reservationRequest.getTheme(),
                reservationRequest.getTime()
        );

		ReservationResponseDto reservation = reservationService.save(requestWithName, member != null ? member.getId() : null);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @PostMapping("/admin/reservations")
	public ResponseEntity adminCreate(@RequestBody @Valid ReservationRequestDto reservationRequest) {
        if (reservationRequest.getName() == null || reservationRequest.getName().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
		ReservationResponseDto reservation = reservationService.save(reservationRequest, null);
        return ResponseEntity.created(URI.create("/admin/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/admin/reservations/{id}")
    public ResponseEntity adminDelete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

	@GetMapping("/reservations-mine")
	public ResponseEntity<List<MyReservationResponseDto>> mine(LoginMemberDto member) {
        return ResponseEntity.ok(myReservationService.findMine(member.getId()));
    }
}
