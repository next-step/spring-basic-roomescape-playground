package roomescape.reservation;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.Authorized;
import roomescape.auth.AuthorizedMember;
import roomescape.member.Member;

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

    @Authorized
    @GetMapping("/reservations-mine")
    public List<MyReservationResponse> listMine(AuthorizedMember member) {
        return reservationService.findMine(member.id());
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> create(
            Optional<AuthorizedMember> authorizedMember,
            @RequestBody ReservationRequest reservationRequest
    ) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        boolean isAdmin = authorizedMember
                .map(member -> member.role() == Member.Role.ADMIN)
                .orElse(false);

        if (reservationRequest.getName() == null && reservationRequest.getMemberId() == null) {
            if(authorizedMember.isEmpty()) return ResponseEntity.badRequest().build();
            reservationRequest.setMemberId(authorizedMember.get().id());
        } else {
            if(!isAdmin) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ReservationResponse reservation = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @Authorized
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(AuthorizedMember member, @PathVariable Long id) {
        reservationService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }
}
