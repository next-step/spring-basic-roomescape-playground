package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(
            @RequestBody WaitingRequest waitingRequest,
            LoginMember loginMember
    ) {
        WaitingResponse waiting = waitingService.save(waitingRequest, loginMember);
        return ResponseEntity
                .created(URI.create("/waitings/" + waiting.id()))
                .body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        waitingService.deleteById(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
