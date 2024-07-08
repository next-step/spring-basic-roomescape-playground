package roomescape.domain.time;

import org.springframework.stereotype.Service;
import roomescape.domain.reservation.entity.Reservation;

import java.util.List;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.time.entity.Time;
import roomescape.domain.time.repository.TimeRepository;

@Service
public class TimeService {

    private final TimeRepository timeRepository;
    private final ReservationRepository reservationRepository;

    public TimeService(final TimeRepository timeRepository,
                       final ReservationRepository reservationRepository) {
        this.timeRepository = timeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
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

    /**
     * 컨트롤러 계층에 DTO를 반환하도록 리팩토링
     * https://tecoble.techcourse.co.kr/post/2021-04-25-dto-layer-scope/
     */
    public List<Time> findAll() {
        return timeRepository.findAll();
    }

    public Time save(Time time) {
        return timeRepository.save(time);
    }

    public void deleteById(Long id) {
        timeRepository.deleteById(id);
    }
}
