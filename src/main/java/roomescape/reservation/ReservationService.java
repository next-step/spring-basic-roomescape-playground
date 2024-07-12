package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            TimeRepository timeRepository,
            ThemeRepository themeRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Reservation reservation = reservationRepository.save(requestToDao(reservationRequest));

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private Reservation requestToDao(ReservationRequest request) {
        return new Reservation(request.name(), request.date(), timeRepository.findById(request.time()).get(), themeRepository.findById(request.theme()).get());

    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .toList()
                .stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findReservationsByName(String name) {
        List<Reservation> reservations = reservationRepository.findByName(name);
        return reservations.stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getDate(),
                        reservation.getTime().getValue(),
                        "예약"))
                .toList();
    }
}
