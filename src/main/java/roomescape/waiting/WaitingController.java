package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.login.LoginMember;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse>create(
            @RequestBody WaitingRequest waitingRequest,
            LoginMember loginMember
    ){
        WaitingResponse waitingResponse= waitingService.save(waitingRequest,loginMember);

        return ResponseEntity
                .created(URI.create("/waitings/"+ waitingResponse.getId()))
                .body(waitingResponse);
    }
}
