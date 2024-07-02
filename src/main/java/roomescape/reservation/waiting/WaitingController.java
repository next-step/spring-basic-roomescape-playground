package roomescape.reservation.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.LoginMember;

import java.net.URI;

@RequestMapping("/waitings")
//@RequiredArgsConstructor
@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    //예약 대기 생성해줘야함
    @PostMapping
    public ResponseEntity createWaitings(@RequestBody WaitingRequest waitingRequest, LoginMember loginMember) {
        WaitingResponse waiting = waitingService.createWaiting(waitingRequest, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }

    //받은아이디기반으로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity cancelWaiting(@PathVariable Long id) {
        waitingService.cancelWaiting(id);
        return ResponseEntity.noContent().build();
    }
}
