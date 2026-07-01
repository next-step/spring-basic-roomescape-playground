package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.dto.LoginMember;
import roomescape.time.AvailableTime;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(@RequestBody ReservationRequest request, LoginMember loginMember) {
        if (request.date() == null
                || request.themeId() == null
                || request.timeId() == null) {
            return ResponseEntity.badRequest().build();
        } else if (request.name() == null) {
            request = new ReservationRequest(loginMember.name(), request.date(), request.themeId(), request.timeId());
        }
        ReservationResponse response = reservationService.registerReservation(loginMember.name(), request);

        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> getMyReservations(LoginMember loginMember) {
        return ResponseEntity.ok(reservationService.getMyReservations(loginMember.name()));
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(reservationService.getAvailableTime(date, themeId));
    }
}
