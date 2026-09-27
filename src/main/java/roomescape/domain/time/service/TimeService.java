package roomescape.domain.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.entity.AvailableTime;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;
import roomescape.global.exception.ConflictException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class TimeService {

    private TimeRepository timeRepository;
    private ReservationRepository reservationRepository;

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
                        time.getTimeValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    @Transactional
    public Time save(Long requesterId, LocalTime value) {

        if (timeRepository.existsByTimeValue(value)) {
            throw new ConflictException(requesterId, Map.of("value", value), "이미 존재하는 시간입니다.");
        }

        Time time = new Time(value);
        return timeRepository.save(time);
    }

    @Transactional
    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
