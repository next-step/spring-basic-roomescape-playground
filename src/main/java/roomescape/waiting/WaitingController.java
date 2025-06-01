package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> waiting(LoginMember loginMember, @RequestBody WaitingRequest waitingRequest) throws MalformedURLException, URISyntaxException {

        Long rank = waitingService.create(loginMember.getId(), waitingRequest);

        return ResponseEntity
                .created(URI.create("/"))
                .body(new WaitingResponse(rank));
    }

    @DeleteMapping("/waitings/{waitingId}")
    public ResponseEntity<WaitingResponse> cancel(LoginMember loginMember, @PathVariable("waitingId") Long waitingId) {

        waitingService.cancel(loginMember.getId(), waitingId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
