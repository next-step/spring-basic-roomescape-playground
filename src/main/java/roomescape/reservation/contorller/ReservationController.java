package roomescape.reservation.contorller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import roomescape.auth.Auth;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.waiting.service.WaitingService;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final WaitingService waitingService;

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(
        @RequestBody ReservationRequest reservationRequest,
        @Auth LoginMember loginMember
    ) {
        if (reservationRequest.date() == null
            || reservationRequest.theme() == null
            || reservationRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }
        ReservationResponse reservation = reservationService.save(reservationRequest, loginMember);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> listMine(@Auth LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationService.findMyReservations(loginMember);
        List<MyReservationResponse> waitings = waitingService.findMyReservations(loginMember);

        List<MyReservationResponse> combinedReservations = new ArrayList<>();
        combinedReservations.addAll(reservations);
        combinedReservations.addAll(waitings);
        return ResponseEntity.ok(combinedReservations);
    }
}
