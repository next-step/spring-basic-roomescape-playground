package roomescape.reservationTime;

import java.time.LocalDate;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.reservation.Reservation;

import java.util.List;
import roomescape.reservation.ReservationRepository;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationRepository reservationRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<AvailableTime> getAvailableTime(LocalDate date, Long themeId) {
        List<Reservation> reservations = reservationRepository.findByDateAndTheme_Id(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(reservationTime -> toAvailableTime(reservationTime, reservations))
                .toList();
    }

    private AvailableTime toAvailableTime(ReservationTime reservationTime
            , List<Reservation> reservations) {
        boolean isBooked = isTimeBooked(reservationTime, reservations);
        return new AvailableTime(
                reservationTime.getId(),
                reservationTime.getTimeValue().toString(),
                isBooked
        );
    }

    private boolean isTimeBooked(ReservationTime reservationTime, List<Reservation> reservations) {
        return reservations.stream()
                .anyMatch(reservation -> reservationTime.isSame(reservation.getTime()));
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeRepository.findAll()
                .stream()
                .map(ReservationTimeResponse::new)
                .toList();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.getTimeValue() == null) {
            throw new RoomescapeBadRequestException("잘못된 예약 시간 정보입니다.");
        }

        return reservationTimeRepository.save(reservationTime);
    }

    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
