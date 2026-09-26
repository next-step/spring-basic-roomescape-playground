package roomescape.waiting.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.domain.LoginMember;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.service.WaitingCommand;
import roomescape.waiting.service.WaitingResult;
import roomescape.waiting.service.WaitingService;

import java.net.URI;

@RestController
public class WaitingController {
    private final WaitingService waitingService;

    public WaitingController(WaitingService waitingService) {
        this.waitingService = waitingService;
    }

    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> create(@Valid @RequestBody WaitingRequest request,
                                                  LoginMember loginMember) {
        WaitingCommand command = new WaitingCommand(request.date(), request.themeId(), request.timeId());
        WaitingResult waiting = waitingService.save(command, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + waiting.id()))
                .body(toResponse(waiting));
    }

    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, LoginMember loginMember) {
        waitingService.delete(id, loginMember);
        return ResponseEntity.noContent().build();
    }

    private WaitingResponse toResponse(WaitingResult result) {
        return new WaitingResponse(
                result.id(),
                result.theme(),
                result.date(),
                result.time(),
                result.waitingNumber()
        );
    }
}
