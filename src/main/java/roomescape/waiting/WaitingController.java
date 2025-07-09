package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.dto.LoginMember;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

import java.net.URI;

@RestController
@RequestMapping("/waitings")
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest request, LoginMember loginMember) {
        WaitingResponse response = waitingService.create(request, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + response.getId())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long id, LoginMember loginMember) {
        waitingService.delete(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
