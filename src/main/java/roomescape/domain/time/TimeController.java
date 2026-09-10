package roomescape.domain.time;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
public class TimeController {
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<TimeResponse> list() {
        return timeService.findAll().stream().map(TimeResponse::from).toList();
    }

    @PostMapping("/times")
    public ResponseEntity<TimeResponse> create(@Valid @RequestBody TimeRequest request) {

        Time newTime = timeService.save(request.value());

        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(TimeResponse.from(newTime));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(
            @RequestParam @FutureOrPresent(message = "날짜는 과거일 수 없습니다.") LocalDate date,
            @RequestParam Long themeId
    ) {
        return ResponseEntity.ok(timeService.getAvailableTime(date, themeId));
    }
}
