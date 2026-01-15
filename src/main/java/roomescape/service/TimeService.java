package roomescape.service;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.repository.TimeRepository;
import roomescape.dto.TimeRequest;
import roomescape.dto.TimeResponse;
import roomescape.model.Reservation;
import roomescape.repository.ReservationRepository;

import java.util.List;
import roomescape.dto.AvailableTime;
import roomescape.model.Time;

@Service
@Transactional
public class TimeService {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
        List<Time> times = timeRepository.findAll();

        return times.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<TimeResponse> findAll() {
        return timeRepository.findAll().stream().map((time) -> new TimeResponse(time.getId(), time.getValue())).toList();
    }

    public TimeResponse create(TimeRequest request) {
        Time time = timeRepository.save(new Time(request.value()));

        return new TimeResponse(time.getId(), time.getValue());
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
