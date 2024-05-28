package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.MemberResponse;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest waitingRequest, MemberResponse memberResponse) {
        WaitingResponse waitingResponse = waitingService.createWaiting(memberResponse, waitingRequest);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.getId())).body(waitingResponse);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity deleteWaiting(@PathVariable Long id, MemberResponse memberResponse) {
        waitingService.deleteWaiting(id, memberResponse);
        return ResponseEntity.noContent().build();
    }
}
