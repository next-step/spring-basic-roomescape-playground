package roomescape.reservation;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import roomescape.global.auth.JwtService;
import roomescape.member.dto.LoginMember;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.dto.WaitingWithRank;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class ReservationService {

    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;

    private JwtService jwtService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, WaitingRepository waitingRepository, JwtService jwtService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
        this.jwtService = jwtService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {

        Optional<Time> timeOptional = timeRepository.findById(reservationRequest.getTime());
        Optional<Theme> themeOptional = themeRepository.findById(reservationRequest.getTheme());

        if (timeOptional.isEmpty() || themeOptional.isEmpty()) return null;

        Reservation reservation= reservationRepository.save(new Reservation(reservationRequest.getName(), reservationRequest.getDate(),timeOptional.get(), themeOptional.get()));
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {

        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(reservation -> new MyReservationResponse(reservation.getId(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue(), "예약"))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingWithRankByMemberId(loginMember.getId()).stream()
                .map(it -> new MyReservationResponse(it.getWaiting().getId(), it.getWaiting().getTheme().getName(),  it.getWaiting().getDate(), it.getWaiting().getTime().getValue(), (it.getRank() + 1) + "번째 예약대기"))
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream())
                .sorted(Comparator.comparing(MyReservationResponse::getDate))
                .toList();
    }
}
