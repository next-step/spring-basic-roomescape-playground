package roomescape.waiting;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.JwtUtil;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/waitings")
public class WaitingController {
    private final WaitingService waitingService;
    private final JwtUtil jwtUtil;

    public WaitingController(WaitingService waitingService, JwtUtil jwtUtil) {
        this.waitingService = waitingService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<WaitingResponse> createWaiting(@RequestBody WaitingRequest waitingRequest, HttpServletRequest request) {
        String token = request.getCookies() != null ? request.getCookies()[0].getValue() : "";
        Long memberId = jwtUtil.getUserIdFromToken(token);
        Waiting waiting = waitingService.createWaiting(memberId, waitingRequest.getDate(), waitingRequest.getTimeId(), waitingRequest.getThemeId());
        return new ResponseEntity<>(new WaitingResponse(waiting.getId()), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deleteWaiting(@PathVariable Long id) {
        waitingService.deleteWaiting(id);
    }

    @GetMapping
    public List<WaitingResponseWithRank> getWaitings(HttpServletRequest request) {
        String token = request.getCookies() != null ? request.getCookies()[0].getValue() : "";
        Long memberId = jwtUtil.getUserIdFromToken(token);
        List<WaitingWithRank> waitingsWithRank = waitingService.getWaitingsWithRankByMemberId(memberId);
        return waitingsWithRank.stream()
                .map(wr -> new WaitingResponseWithRank(wr.getWaiting().getId(), wr.getWaiting().getThemeId().toString(), wr.getWaiting().getDate(), wr.getWaiting().getTimeId().toString(), wr.getStatus()))
                .collect(Collectors.toList());
    }
}
