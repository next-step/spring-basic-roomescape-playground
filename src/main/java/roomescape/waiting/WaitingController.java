package roomescape.waiting;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.LoginMember;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(
            @RequestBody WaitingRequest waitingRequest,
            LoginMember loginMember
    ) {
        WaitingResponse response = waitingService.createWaiting(waitingRequest, loginMember.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
