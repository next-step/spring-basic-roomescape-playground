package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.LoginMember;
import roomescape.auth.LoginUser;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@LoginUser LoginMember loginMember, @RequestBody WaitingRequest waitingRequest) {
        if(waitingRequest.getDate() == null || waitingRequest.getTheme() == null || waitingRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        if(waitingRequest.getName() == null) {
            waitingRequest.setName(loginMember.getName());
        }

        WaitingResponse waitingResponse = waitingService.save(waitingRequest);

        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.getId())).body(waitingResponse);
    }


}
