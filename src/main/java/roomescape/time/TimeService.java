package roomescape.time;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.waiting.WaitingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class TimeService {
    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;

    public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository, WaitingRepository waitingRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
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
        Time time = timeRepository.findById(id)
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.TIME_NOT_FOUND));

        boolean existsInReservation = reservationRepository.existsByTimeId(time.getId());
        boolean existsInWaiting = waitingRepository.existsByTimeId(time.getId());

        if (existsInReservation || existsInWaiting) {
            throw new RoomEscapeException(ErrorCode.DELETE_CONFLICT, "시간이 다른 자원에서 사용중입니다.");
        }
        time.softDelete();
    }
}
