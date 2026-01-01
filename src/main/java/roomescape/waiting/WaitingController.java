package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;
import roomescape.reservation.ReservationRequest;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(LoginMember loginMember, @RequestBody ReservationRequest request) {
        WaitingResponse response = waitingService.createWaiting(loginMember, request);
        return ResponseEntity.created(URI.create("/waitings/" + response.getId())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(LoginMember loginMember, @PathVariable Long id) {
        waitingService.deleteWaiting(id);
        return ResponseEntity.noContent().build();
    }
}
