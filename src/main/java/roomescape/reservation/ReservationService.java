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
        Member member = findMemberById(loginMember);
        Time time = findTimeById(reservationRequest);
        Theme theme = findThemeById(reservationRequest);

        Reservation reservation = new Reservation(reservationRequest.getDate(), time, theme, member);

        validateReservationPermission(reservation, loginMember);

        return saveReservation(reservation);
    }

    public void deleteById(Long id, LoginMember loginMember) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));

        validateReservationPermission(reservation, loginMember);

        reservationRepository.deleteById(id);
    }

    private void validateReservationPermission(Reservation reservation, LoginMember loginMember) {
        if (loginMember.notHaveName(reservation.getMember().getName()) && loginMember.isNotAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, ErrorMessage.FORBIDDEN_RESERVATION.getMessage());
        }
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new ReservationResponse(reservation.getId(), reservation.getMember().getName(),
                        reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue()))
                .toList();
    }

    public List<UserReservationResponse> findMyAllReservations(LoginMember loginMember) {
        List<UserReservationResponse> reservations = findUserReservations(loginMember);
        List<UserReservationResponse> waitings = findUserWaitings(loginMember);

        return mergeAndSortReservationsByDate(reservations, waitings);
    }

    private Member findMemberById(LoginMember loginMember) {
        return memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Time findTimeById(ReservationRequest reservationRequest) {
        return timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
    }

    private Theme findThemeById(ReservationRequest reservationRequest) {
        return themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));
    }

    private ReservationResponse saveReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);

        return new ReservationResponse(savedReservation.getId(), savedReservation.getMember().getName(),
                savedReservation.getTheme().getName(), savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }

    private List<UserReservationResponse> findUserReservations(LoginMember loginMember) {
        return reservationRepository.findByMemberId(loginMember.id()).stream()
                .filter(reservation -> reservation.isSame(loginMember.id()))
                .map(reservation -> new UserReservationResponse(reservation.getId(), reservation.getTheme().getName(),
                        reservation.getDate(), reservation.getTime().getValue(), Status.RESERVATION.getDescription()))
                .toList();
    }

    private List<UserReservationResponse> findUserWaitings(LoginMember loginMember) {
        return waitingRepository.findWaitingsWithRankByMemberId(loginMember.id()).stream()
                .map(waitingWithRank -> new UserReservationResponse(waitingWithRank.getWaiting().getId(),
                        waitingWithRank.getWaiting().getTheme().getName(), waitingWithRank.getWaiting().getDate(),
                        waitingWithRank.getWaiting().getTime(),
                        (waitingWithRank.getRank() + 1) + "번째 " + Status.WAIT.getDescription()))
                .toList();
    }

    private List<UserReservationResponse> mergeAndSortReservationsByDate(List<UserReservationResponse> reservations,
                                                                         List<UserReservationResponse> waitings) {
        return Stream.concat(reservations.stream(), waitings.stream())
                .sorted(Comparator.comparing(UserReservationResponse::getDate))
                .toList();
    }
}
