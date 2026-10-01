package roomescape.domain.time.web.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.web.support.annotation.AdminOnly;
import roomescape.domain.auth.web.support.annotation.Login;
import roomescape.domain.auth.web.support.annotation.Public;
import roomescape.domain.time.entity.AvailableTime;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.service.TimeService;
import roomescape.domain.time.web.dto.TimeRequest;
import roomescape.domain.time.web.dto.TimeResponse;
import roomescape.global.exception.ConflictException;

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

    @Public
    @GetMapping("/times")
    public List<TimeResponse> list() {
        return timeService.findAll().stream().map(TimeResponse::from).toList();
    }

    @AdminOnly
    @PostMapping("/times")
    public ResponseEntity<TimeResponse> create(
            @Valid @RequestBody TimeRequest request,
            @Login LoginMember loginMember
    ) {
        Time newTime;

        try {
            newTime = timeService.save(request.value());
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException("이미 존재하는 시간입니다.");
        }

        return ResponseEntity.created(URI.create("/times/" + newTime.getId())).body(TimeResponse.from(newTime));
    }

    @AdminOnly
    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        timeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Public
    @GetMapping("/available-times")
    public ResponseEntity<List<AvailableTime>> availableTimes(
            @RequestParam @FutureOrPresent(message = "날짜는 과거일 수 없습니다.") LocalDate date,
            @RequestParam Long themeId
    ) {
        return ResponseEntity.ok(timeService.getAvailableTime(date, themeId));
    }
}
