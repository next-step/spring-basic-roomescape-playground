package roomescape.reservation;

import org.springframework.stereotype.Service;
import java.util.List;
import roomescape.global.exception.RoomescapeNotFoundException;
import roomescape.reservationTime.ReservationTimeRepository;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;
import roomescape.theme.ThemeRepository;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationService(ReservationRepository reservationRepository, ThemeRepository themeRepository,
                              ReservationTimeRepository reservationTimeRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new RoomescapeNotFoundException("테마를 찾을 수 없습니다."));
        ReservationTime reservationTime = reservationTimeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new RoomescapeNotFoundException("예약 시간을 찾을 수 없습니다."));

        Reservation reservation = reservationRepository.save(reservationRequest.toReservation(theme, reservationTime));
        return new ReservationResponse(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithReservationTime().stream()
                .map(reservation -> new ReservationResponse(reservation))
                .toList();
    }
}
