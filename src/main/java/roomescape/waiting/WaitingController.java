package roomescape.waiting;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest waitingRequest,
                                                  LoginMember loginMember) {
        WaitingResponse waitingResponse = waitingService.create(waitingRequest, loginMember.email());
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id())).body(waitingResponse);
    }
}
