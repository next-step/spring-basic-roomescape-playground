package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.LoginMember;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.service.ReservationCreateService;
import roomescape.reservation.service.ReservationDeleteService;
import roomescape.reservation.service.ReservationFindService;

@RestController
public class ReservationController {
    private final ReservationCreateService createService;
    private final ReservationFindService findService;
    private final ReservationDeleteService deleteService;

    public ReservationController(ReservationCreateService createService, ReservationFindService findService,
                                 ReservationDeleteService deleteService) {
        this.createService = createService;
        this.findService = findService;
        this.deleteService = deleteService;
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<ReservationResponse>> showMyReservations(LoginMember loginMember) {
        List<ReservationResponse> reservationResponses = findService.findReservations(loginMember);

        return ResponseEntity.ok().body(reservationResponses);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createUserReservation(@Valid @RequestBody ReservationRequest request, LoginMember loginMember) {
        ReservationResponse response = createService.saveUserReservation(request, loginMember);

        return ResponseEntity.created(URI.create("/reservations/" + response.getId()))
                .body(response);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        deleteService.deleteReservation(id, loginMember);

        return ResponseEntity.noContent().build();
    }
}
