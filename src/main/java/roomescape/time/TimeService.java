package roomescape.time;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
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
                        time.getValue(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<TimeResponse> findAll() {
        return timeRepository.findAll().stream()
                .map(TimeResponse::from)
                .toList();
    }

    public TimeResponse save(TimeRequest timeRequest) {

        try {
            Time savedTime = timeRepository.save(timeRequest.toEntity());
            return TimeResponse.from(savedTime);
        } catch (DataIntegrityViolationException e) {
            throw new RoomEscapeException(ErrorCode.DUPLICATE_TIME, "이미 존재하는 시간입니다.");
        }
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
