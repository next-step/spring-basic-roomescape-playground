package roomescape.waiting;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.member.LoginMember;
import roomescape.reservation.MyReservationResponse;
import java.net.URI;
import java.util.List;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @GetMapping("/waitings")
    public ResponseEntity<List<MyReservationResponse>> getWaitings(@RequestParam Long memberId) {
        List<WaitingWithRank> waitings = waitingService.findWaitingsWithRankByMemberId(memberId);
        List<MyReservationResponse> responses = waitings.stream()
                .map(MyReservationResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest request, LoginMember loginMember) {
        WaitingResponse response = waitingService.createWaiting(request, loginMember.getId());
        return ResponseEntity.created(URI.create("/waitings/" + response.getId())).body(response);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteWaiting(@PathVariable Long id, LoginMember loginMember) {
        waitingService.deleteWaiting(id, loginMember);
        return ResponseEntity.noContent().build();
    }
}
