package roomescape.waiting;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.Authentication;
import roomescape.auth.MemberAuthContext;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@Authentication MemberAuthContext authContext,
                                                  @RequestBody WaitingRequest waitingRequest) {
        if (waitingRequest.date() == null
                || waitingRequest.theme() == null
                || waitingRequest.time() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (waitingRequest.name() == null) {
            waitingRequest = new WaitingRequest(
                    authContext.name(),
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
