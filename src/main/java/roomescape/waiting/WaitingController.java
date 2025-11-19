package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.LoginMember;

@Controller
@RequestMapping("/waitings")
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping
    public ResponseEntity<WaitingResponse> create(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        WaitingResponse response = waitingService.create(waitingRequest, loginMember);
        return ResponseEntity.created(null).body(response);
    }
}
