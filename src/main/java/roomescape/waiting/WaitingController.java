package roomescape.waiting;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;

import java.net.URI;

@RestController
public class WaitingController {
    private WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        WaitingResponse waitingResponse = waitingService.save(waitingRequest, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.id())).body(waitingResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}