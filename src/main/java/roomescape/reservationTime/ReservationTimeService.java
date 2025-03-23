package roomescape.reservationTime;

import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;

import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeRepository reservationTimeRepository;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeRepository reservationTimeRepository,
                                  ReservationDao reservationDao) {
        this.reservationTimeRepository = reservationTimeRepository;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findAllReservationsByDateAndTheme(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();

        return reservationTimes.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getTimeValue().toString(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<ReservationTime> findAll() {
        return reservationTimeRepository.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.getId() == null || reservationTime.getTimeValue() == null) {
            throw new RoomescapeBadRequestException("잘못된 예약 시간 정보입니다.");
        }
        return reservationTimeRepository.save(reservationTime);
    }

    public void deleteById(Long id) {
        reservationTimeRepository.deleteById(id);
    }
}
