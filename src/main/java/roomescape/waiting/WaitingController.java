package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.Member;

import java.net.URI;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, Member member) {

        WaitingResponse waitingResponse = waitingService.save(waitingRequest, member);

        return ResponseEntity.created(URI.create("/waitings/" + waitingResponse.getId()))
                .body(waitingResponse);
    }

    @PostMapping("/waitings/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
