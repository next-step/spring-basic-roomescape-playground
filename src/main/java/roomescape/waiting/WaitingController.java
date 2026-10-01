package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.login.LoginMember;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest request, LoginMember loginMember) {
        WaitingResponse waiting = waitingService.save(request, loginMember);

        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        waitingService.delete(id, loginMember);

        return ResponseEntity.noContent().build();
    }
}
