package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest request, LoginMember loginMember) {
        WaitingResponse response = waitingService.addWaiting(request, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + response.getId())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long id, LoginMember loginMember) {
        waitingService.deleteWaiting(id, loginMember);
        return ResponseEntity.noContent().build();
    }

}
