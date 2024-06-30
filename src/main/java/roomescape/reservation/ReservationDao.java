package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationDao {

    private final ReservationRepository reservationRepository;


    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation save(ReservationRequest reservationRequest) {
        return reservationRepository.save(reservationRequest);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
//
//    public List<Reservation> findReservationsByDateAndTheme(String date, Theme theme) {
//        return reservationRepository.findReservationsByDateAndTheme(date, theme);
//    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }
}
