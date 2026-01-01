package roomescape.reservation.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.AuthMember;
import roomescape.member.Member;
import roomescape.reservation.dto.ReservationRequest;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@AuthMember Member member,
                                                  @RequestBody ReservationRequest request) {
        WaitingResponse response = waitingService.save(request, member);
        return ResponseEntity.created(URI.create("/waitings/" + response.id()))
                .body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}