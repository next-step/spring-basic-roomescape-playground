package roomescape.reservation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.dto.LoginMember;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.WaitingRequest;
import roomescape.reservation.dto.WaitingResponse;
import roomescape.time.model.AvailableTime;

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
        ReservationResponse response = reservationService.registerReservation(loginMember.id(), request);

        return ResponseEntity.created(URI.create("/reservations/" + response.id())).body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest request, LoginMember loginMember) {
        if (request.date() == null
                || request.theme() == null
                || request.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationRequest reservationRequest = new ReservationRequest(
                loginMember.name(),
                request.date(),
                request.theme(),
                request.time()
        );
        WaitingResponse response = reservationService.waitReservation(loginMember.id(), reservationRequest);

        return ResponseEntity.created(URI.create("/waitings")).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> getMyReservations(LoginMember loginMember) {
        return ResponseEntity.ok(reservationService.getMyReservations(loginMember.id()));
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(reservationService.getAvailableTime(date, themeId));
    }
}
