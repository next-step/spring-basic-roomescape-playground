package roomescape.reservation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                              TimeRepository timeRepository, ThemeRepository themeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (loginMember.notHaveName(reservationRequest.getName()) && loginMember.isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN_RESERVATION.getMessage());
        }

        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));

        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, member);
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(),
                savedReservation.getTheme().getName(), savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));

        if (loginMember.isNotAdmin() && loginMember.notHaveName(reservation.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN_DELETE.getMessage());
        }

        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new ReservationResponse(reservation.getId(), reservation.getName(),
                        reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyAllReservations(LoginMember loginMember) {
        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.id()).stream()
                .filter(reservation -> reservation.isSame(loginMember.id()))
                .map(reservation -> new MyReservationResponse(reservation.getId(), reservation.getTheme().getName(),
                        reservation.getDate(), reservation.getTime().getValue(), Status.RESERVATION.getDescription()))
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(loginMember.id()).stream()
                .map(waitingWithRank -> new MyReservationResponse(waitingWithRank.getWaiting().getId(),
                        waitingWithRank.getWaiting().getTheme().getName(), waitingWithRank.getWaiting().getDate(),
                        waitingWithRank.getWaiting().getTime(), (waitingWithRank.getRank()+1) + "번째 " + Status.WAIT.getDescription()))
                .toList();

        List<MyReservationResponse> results = Stream.concat(reservations.stream(), waitings.stream())
                .sorted(Comparator.comparing(MyReservationResponse::getDate))
                .toList();

        return results;
    }
}
