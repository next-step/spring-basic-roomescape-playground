package roomescape.waiting;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.reservation.ReservationRequest;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(
            @RequestBody ReservationRequest reservationRequest,
            LoginMember loginMember
    ) {
        WaitingResponse waiting = waitingService.save(reservationRequest, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        waitingService.delete(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
