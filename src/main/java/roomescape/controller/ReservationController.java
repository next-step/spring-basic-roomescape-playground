package roomescape.controller;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import roomescape.auth.LoginMember;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.model.Member;
import roomescape.service.ReservationService;

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
    public ResponseEntity<ReservationResponse> create(
            @RequestBody ReservationRequest request,
            @LoginMember(required = false) Member member
    ) {
        if (request.date() == null
                || request.theme() == null
                || request.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        // request body에서 찾고 없으면 member
        String name = Optional.ofNullable(request.name()).orElseGet(() -> member != null ? member.getName() : null);

        if (name == null) return ResponseEntity.badRequest().build();

        ReservationRequest finalizedRequest = new ReservationRequest(name, request.date(), request.theme(), request.time());

        ReservationResponse reservation = reservationService.save(finalizedRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
