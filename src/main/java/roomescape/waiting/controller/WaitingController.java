package roomescape.waiting.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.global.login.LoginMemberName;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.waiting.controller.dto.WaitingResponse;
import roomescape.waiting.controller.dto.WaitingWithRank;
import roomescape.waiting.service.WaitingService;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> waiting(@LoginMemberName String memberName,
                                                   @RequestBody ReservationRequest reservationRequest) {
        WaitingWithRank waiting = waitingService.waiting(memberName, reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.LOCATION, "/waitings/" + waiting.getWaiting().getId())
                .body(new WaitingResponse(waiting.getWaiting().getId(), waiting.getRank()));
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> cancel(@LoginMemberName String memberName,
                                       @PathVariable("id") Long id) {
        waitingService.cancel(memberName, id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }
}
