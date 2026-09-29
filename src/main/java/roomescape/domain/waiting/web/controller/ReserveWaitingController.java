package roomescape.domain.waiting.web.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.web.support.annotation.Login;
import roomescape.domain.auth.web.support.annotation.LoginRequired;
import roomescape.domain.waiting.service.ReserveWaitingService;
import roomescape.domain.waiting.service.result.WaitingWithRank;
import roomescape.domain.waiting.web.dto.WaitingRequest;
import roomescape.domain.waiting.web.dto.WaitingResponse;
import roomescape.global.exception.ConflictException;

import java.net.URI;
import java.util.Map;

@RestController
public class ReserveWaitingController {

    private final ReserveWaitingService reserveWaitingService;

    public ReserveWaitingController(ReserveWaitingService reserveWaitingService) {
        this.reserveWaitingService = reserveWaitingService;
    }

    @LoginRequired
    @PostMapping("/waitings")
    public ResponseEntity<WaitingResponse> postReserveWaiting(
            @Login LoginMember loginMember,
            @RequestBody @Valid WaitingRequest request
    ) {
        WaitingWithRank newReserveWaiting;

        try {
             newReserveWaiting = reserveWaitingService.createReserveWaiting(loginMember.id(), request.date(), request.time(), request.theme());
        } catch(DataIntegrityViolationException e) {
            throw new ConflictException(loginMember.id(), Map.of("date", request.date(), "timeId", request.time(), "themeId", request.theme()), "이미 예약 대기가 존재합니다.");
        }

        return ResponseEntity.created(URI.create("/waitings/" + newReserveWaiting.reserveWaiting().getId()))
                .body(WaitingResponse.from(newReserveWaiting));
    }

    @LoginRequired
    @DeleteMapping("/waitings/{id}")
    public ResponseEntity<Void> deleteReserveWaiting(
            @Login LoginMember loginMember,
            @PathVariable(name = "id") Long reserveWaitingId
    ) {
        reserveWaitingService.deleteReserveWaiting(loginMember.id(), reserveWaitingId);

        return ResponseEntity.noContent().build();
    }
}
