package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import roomescape.auth.AdminRoute;
import roomescape.dto.AvailableTime;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.exception.BadRequestException;
import roomescape.service.TimeService;

@RestController
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<TimeResponse> list() {
        return timeService.findAll();
    }

    @AdminRoute
    @PostMapping("/times")
    public ResponseEntity<TimeResponse> create(@RequestBody TimeRequest request) {
        if (!StringUtils.hasText(request.value())) {
            throw new BadRequestException("필수 값이 누락되었습니다.");
        }

        TimeResponse time = timeService.create(request);
        return ResponseEntity.created(URI.create("/times/" + time.id())).body(time);
    }

    @AdminRoute
    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(@RequestParam String date, @RequestParam Long themeId) {
        return ResponseEntity.ok(timeService.getAvailableTime(date, themeId));
    }
}