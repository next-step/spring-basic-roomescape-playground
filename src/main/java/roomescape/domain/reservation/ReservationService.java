package roomescape.domain.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @Transactional
    public Reservation save(String name, LocalDate date, Long themeId, Long timeId) {
        return reservationDao.save(name, date, themeId, timeId);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }
}
