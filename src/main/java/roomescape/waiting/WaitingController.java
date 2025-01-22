package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.authentication.MemberAuthInfo;

import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WaitingController {

    private final WaitingService waitingService;

    @PostMapping("/waitings")
    public ResponseEntity createWaiting(@RequestBody WaitingRequest waitingRequest, MemberAuthInfo memberAuthInfo) {
        WaitingResponse response = waitingService.createWaiting(waitingRequest, memberAuthInfo);
        return ResponseEntity.created(URI.create("/waitings/" + response.id())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity cancelWaiting(@PathVariable Long id) {
        waitingService.cancelWaiting(id);
        return ResponseEntity.noContent().build();
    }
}
