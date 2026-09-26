package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import roomescape.time.repository.AvailableTimeProjection;
import roomescape.time.repository.TimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationAvailabilityService {
    private final TimeRepository timeRepository;

    public ReservationAvailabilityService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<AvailableTimeResult> findAvailableTimes(LocalDate date, Long themeId) {
        return timeRepository.findAvailableTimes(date, themeId).stream()
                .map(this::toResult)
                .toList();
    }

    private AvailableTimeResult toResult(AvailableTimeProjection projection) {
        return new AvailableTimeResult(
                projection.timeId(),
                projection.time(),
                projection.booked()
        );
    }
}
