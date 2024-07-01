package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ReservationDao {

    private final ReservationRepository reservationRepository;

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return reservationRepository.findByDateAndThemeId(date, themeId);
    }

    public List<Reservation> findByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId);
    }

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
