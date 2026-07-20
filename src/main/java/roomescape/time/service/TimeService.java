package roomescape.time.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ApplicationException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.dto.AvailableTime;
import roomescape.time.dto.TimeResponse;
import roomescape.time.entity.Time;
import roomescape.time.exception.TimeErrorCode;
import roomescape.time.repository.TimeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TimeService {
    private TimeRepository timeRepository;
    private ReservationRepository reservationRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndThemeId(LocalDate.parse(date), themeId);
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

    public List<TimeResponse> findAll() {
        return timeRepository.findAllByDeletedFalse()
                .stream()
                .map(time -> new TimeResponse(
                        time.getId(),
                        time.getTimeValue()
                ))
                .toList();
    }

    @Transactional
    public TimeResponse create(Time time) {
        Time savedTime = timeRepository.save(time);
        return new TimeResponse(savedTime.getId(), savedTime.getTimeValue());
    }

    @Transactional
    public void deleteById(Long id) {
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(TimeErrorCode.TIME_NOT_FOUND));
        time.markDeleted();
    }
}
