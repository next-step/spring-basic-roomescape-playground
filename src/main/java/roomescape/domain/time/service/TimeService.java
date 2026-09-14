package roomescape.domain.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.repository.ReservationDao;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.entity.AvailableTime;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeService {
    private TimeRepository timeRepository;
    private ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationDao reservationRepository) {
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

    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    @Transactional
    public Time save(LocalTime value) {
        Time time = new Time(value);
        return timeRepository.save(time);
    }

    @Transactional
    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
