package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.Authentication;
import roomescape.member.LoginMember;
import java.net.URI;


@Controller
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@Authentication LoginMember loginMember,
                                                  @RequestBody WaitingRequest waitingRequest) {
        if (waitingRequest.date() == null
                || waitingRequest.theme() == null
                || waitingRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (waitingRequest.name() == null) {
            waitingRequest = new WaitingRequest(
                    loginMember.getName(),
                    waitingRequest.date(),
                    waitingRequest.theme(),
                    waitingRequest.time()
            );
        }

        final WaitingResponse response = waitingService.save(waitingRequest);

        return ResponseEntity.created(URI.create("/waitings/" + response.id()))
                .body(response);
    }
}
