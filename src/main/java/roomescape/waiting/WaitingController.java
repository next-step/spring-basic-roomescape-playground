package roomescape.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.member.LoginMember;
import roomescape.reservation.CreateReservationDto;
import roomescape.reservation.ReservationRequest;
import roomescape.reservation.ReservationResponse;
import roomescape.reservation.ReservationService;

import java.net.URI;

@Controller
@RequiredArgsConstructor
public class WaitingController {
    private final WaitingService waitingService;

    @PostMapping("/waitings")
    public ResponseEntity create(@RequestBody WaitingRequest waitingRequest, LoginMember member) {
        if (member == null
                || waitingRequest.getDate() == null
                || waitingRequest.getTheme() == null
                || waitingRequest.getTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        WaitingResponse waiting = waitingService.save(member, waitingRequest);

        return ResponseEntity.created(URI.create("/waitings/" + waiting.getId())).body(waiting);
    }
}
