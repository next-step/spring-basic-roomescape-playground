package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {

        String name;
        if (reservationRequest.getName() == null) {
            name = member.getName();
        }
        else {
            name = reservationRequest.getName();
        }

        Theme theme = themeRepository.getReferenceById(reservationRequest.getTheme());
        Time time = timeRepository.getReferenceById(reservationRequest.getTime());

        Reservation reservation = new Reservation(name, reservationRequest.getDate(), time, theme);
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream().map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue())).toList();
    }

    public List<MyReservationResponse> getMyReservation(LoginMember member) {
        return reservationRepository.findByMemberId(member.getId()).stream().map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), "예약")).toList();
    }
}
