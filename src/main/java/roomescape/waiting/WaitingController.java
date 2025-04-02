package roomescape.waiting;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AuthMember;
import roomescape.member.Member;

@RestController
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingRankingResponse> create(@AuthMember Member member,
                                                         @RequestBody WaitingRequest waitingRequest) {
        WaitingRankingResponse result = waitingService.create(member, waitingRequest);

        return ResponseEntity.created(URI.create("/waitings/" + result.reservationId()))
                .body(result);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@AuthMember Member member, @PathVariable long id) {
        waitingService.deleteById(id, member.getId());
        return ResponseEntity.noContent().build();
    }
}
