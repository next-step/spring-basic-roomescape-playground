package roomescape.waiting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import roomescape.member.LoginMember;
import java.util.List;

@RestController
public class WaitingController {
    @Autowired
    private WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaitingReservation(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        WaitingResponse waiting = waitingService.createWaitingReservation(waitingRequest,loginMember);
        return ResponseEntity.status(HttpStatus.CREATED).body(waiting);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}