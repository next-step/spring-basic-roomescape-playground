package roomescape.reservation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AdminOnly;
import roomescape.auth.AuthUser;
import roomescape.auth.LoginMemberInfo;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/reservations-mine")
    public List<ReservationMineResponse> listMine(@AuthUser LoginMemberInfo loginMember) {
        return reservationService.findMine(loginMember);
    }

    @PostMapping("/reservations")
    public ResponseEntity create(
            @RequestBody ReservationRequest reservationRequest,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey,
            @AuthUser Optional<LoginMemberInfo> loginMember
    ) {
        if (reservationRequest.getDate() == null
                || reservationRequest.getTheme() == null
                || reservationRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        ReservationResponse reservation = reservationService.save(reservationRequest, loginMember, Optional.ofNullable(idempotencyKey));

        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    @AdminOnly
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
