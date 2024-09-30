package roomescape.waiting.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import roomescape.auth.Auth;
import roomescape.member.dto.LoginMember;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.service.WaitingService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/waitings")
public class WaitingController {

    private final WaitingService waitingService;

    @PostMapping()
    public ResponseEntity<WaitingResponse> create(
        @RequestBody WaitingRequest request,
        @Auth LoginMember loginMember
    ) {
        if (request.date() == null
            || request.theme() == null
            || request.time() == null) {
            return ResponseEntity.badRequest().build();
        }
        WaitingResponse response = waitingService.save(request, loginMember);
        return ResponseEntity.created(URI.create("/waitings/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        waitingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
