package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;



@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository,WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Optional<Time> time = timeRepository.findById(reservationRequest.getTime());
        Optional<Theme> theme = themeRepository.findById(reservationRequest.getTheme());

        if (time.isEmpty() || theme.isEmpty()) return null;

        Reservation reservation = reservationRepository.save(new Reservation(reservationRequest.getName(),reservationRequest.getDate(), time.get(),theme.get()));
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {

        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(reservation -> new MyReservationResponse(reservation.getId(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue(), "예약"))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingWithRankByMemberId(loginMember.getId()).stream()
                .map(this::CustomWaitingResponse)
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream())
                .sorted(Comparator.comparing(MyReservationResponse::getDate))
                .toList();
    }

    public MyReservationResponse CustomWaitingResponse(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.waiting().getId(),
                waitingWithRank.waiting().getTheme().getName(),
                waitingWithRank.waiting().getDate(),
                waitingWithRank.waiting().getTime().getValue(),
                (waitingWithRank.rank() + 1) + "번째 예약대기"
        );
    }


    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findReservation(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.getId()).stream()
                .map(x -> new MyReservationResponse(x.getId(), x.getTheme().getName(), x.getDate(), x.getTime().getValue(), "예약"))
                .toList();
        return reservations;
    }


}