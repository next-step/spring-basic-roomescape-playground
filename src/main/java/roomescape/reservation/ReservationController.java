package roomescape.reservation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;

import java.net.URI;
import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;
    private final Validator validator;

    public ReservationController(ReservationService reservationService,
                                 Validator validator) {
        this.reservationService = reservationService;
        this.validator = validator;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@Valid @RequestBody ReservationRequest req, LoginMember loginMember) {

        if (loginMember == null) {
            var violations = validator.validate(req, UserReservation.class);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        }

        ReservationResponse reservation = reservationService.create(req, loginMember);
        return ResponseEntity.created(URI.create("/reservations/" + reservation.getId())).body(reservation);
    }


    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id, LoginMember loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        reservationService.deleteById(id, loginMember.id());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations-mine")
    public ResponseEntity<List<MyReservationResponse>> mine(LoginMember loginMember) {
        if (loginMember == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(reservationService.findMine(loginMember.id())
        );
    }

}
