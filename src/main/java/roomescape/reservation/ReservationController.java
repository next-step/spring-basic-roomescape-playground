package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingService;

import java.net.URI;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final WaitingService waitingService;

    public ReservationController(ReservationService reservationService, WaitingService waitingService) {
        this.reservationService = reservationService;
        this.waitingService = waitingService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@RequestBody ReservationRequest reservationRequest, LoginMember loginMember) {
        if (
                reservationRequest.getDate() == null
                        || reservationRequest.getTheme() == null
                        || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        ReservationResponse reservation = reservationService.save(reservationRequest, loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> findReservationsByMember(LoginMember loginMember) {
        List<MyReservationResponse> reservationByMember = reservationService.findReservationByMember(loginMember);
        List<WaitingResponse> waitingByMember = waitingService.findWaitingWithRankByMember(loginMember);

        List<MyReservationResponse> waiting = waitingByMember.stream()
                .map(MyReservationResponse::from)
                .toList();

        List<MyReservationResponse> all = Stream.concat(
                        reservationByMember.stream(),
                        waiting.stream())
                .toList();


        return ResponseEntity.ok().body(all);
    }
}
