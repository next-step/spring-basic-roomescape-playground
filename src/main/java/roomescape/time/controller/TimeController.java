package roomescape.time.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.auth.AdminOnly;
import roomescape.reservation.service.ReservationAvailabilityService;
import roomescape.reservation.service.AvailableTimeResult;
import roomescape.time.dto.AvailableTimeResponse;
import roomescape.time.dto.TimeRequest;
import roomescape.time.dto.TimeResponse;
import roomescape.time.service.TimeResult;
import roomescape.time.service.TimeService;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
public class TimeController {
    private final TimeService timeService;
    private final ReservationAvailabilityService reservationAvailabilityService;

    public TimeController(TimeService timeService,
                          ReservationAvailabilityService reservationAvailabilityService) {
        this.timeService = timeService;
        this.reservationAvailabilityService = reservationAvailabilityService;
    }

    @GetMapping("/times")
    public List<TimeResponse> list() {
        return timeService.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @PostMapping("/times")
    @AdminOnly
    public ResponseEntity<TimeResponse> create(@Valid @RequestBody TimeRequest request) {
        TimeResult time = timeService.save(request.value());
        return ResponseEntity.created(URI.create("/times/" + time.id())).body(toResponse(time));
    }

    @DeleteMapping("/times/{id}")
    @AdminOnly
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTimeResponse>> availableTimes(@RequestParam LocalDate date,
                                                                      @RequestParam Long themeId) {
        return ResponseEntity.ok(reservationAvailabilityService.findAvailableTimes(date, themeId).stream()
                .map(this::toAvailableTimeResponse)
                .toList());
    }

    private TimeResponse toResponse(TimeResult result) {
        return new TimeResponse(result.id(), result.value());
    }

    private AvailableTimeResponse toAvailableTimeResponse(AvailableTimeResult result) {
        return new AvailableTimeResponse(result.timeId(), result.time(), result.booked());
    }
}
