package roomescape.waiting.controller;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.domain.LoginMember;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.service.WaitingService;
import roomescape.waiting.service.impl.WaitingServiceImpl;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingServiceImpl waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        WaitingResponse waitingResponse = waitingService.createWaiting(waitingRequest, null, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.getId())).body(waitingResponse);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long id) {
        waitingService.deleteWaiting(id);
        return ResponseEntity.noContent().build();
    }
}
