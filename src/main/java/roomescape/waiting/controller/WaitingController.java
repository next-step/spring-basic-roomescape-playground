package roomescape.waiting.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.auth.domain.LoginMember;
import roomescape.auth.web.Login;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.service.WaitingService;

import java.net.URI;

@Controller
public class WaitingController {

    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@Valid @RequestBody WaitingRequest waitingRequest, @Login LoginMember loginMember) {
        WaitingResponse reservation = waitingService.create(waitingRequest, loginMember);

        return ResponseEntity.created(URI.create("/waitings/" + reservation.id())).body(reservation);
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        waitingService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
