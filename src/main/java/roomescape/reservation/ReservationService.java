package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import roomescape.auth.LoginMember;
import roomescape.exception.RoomEscapeException;
import roomescape.member.MemberRepository;
import roomescape.theme.ThemeRepository;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingService;

import java.util.List;
import java.util.stream.Stream;

import static roomescape.exception.ErrorCode.*;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingService waitingService;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingService = waitingService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        reservationRequest = fillMissingNameWithLoginMember(reservationRequest, loginMember);
        validateDuplicatedReservation(reservationRequest, loginMember);

        Reservation reservation = toReservation(reservationRequest, loginMember);
        Reservation savedReservation = reservationRepository.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    public void deleteById(Long id) {
        Reservation findReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RoomEscapeException(RESERVATION_NOT_FOUND));
        reservationRepository.delete(findReservation);
    }


    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<MyReservationResponse> findAllByMember(LoginMember loginMember) {
        return reservationRepository.findByMemberId(loginMember.id()).stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    public List<MyReservationResponse> findMyReservationsAndWaitings(LoginMember loginMember) {
        Stream<MyReservationResponse> reservations = findAllByMember(loginMember).stream();
        Stream<MyReservationResponse> waitings = waitingService.findWaitingWithRankByMember(loginMember).stream()
                .map(MyReservationResponse::from);

        return Stream.concat(reservations, waitings).toList();
    }

    private Reservation toReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        return new Reservation(reservationRequest.name(),
                reservationRequest.date(),
                timeRepository.findById(reservationRequest.time()).orElseThrow(() -> new RoomEscapeException(TIME_NOT_FOUND)),
                themeRepository.findById(reservationRequest.theme()).orElseThrow(() -> new RoomEscapeException(THEME_NOT_FOUND)),
                memberRepository.findById(loginMember.id()).orElseThrow(() -> new RoomEscapeException(MEMBER_NOT_FOUND)));
    }

    private void validateDuplicatedReservation(ReservationRequest reservationRequest, LoginMember loginMember) {
        boolean alreadyReserved = reservationRepository.existsByThemeIdAndDateAndTimeId(
                reservationRequest.theme(),
                reservationRequest.date(),
                reservationRequest.time());
        if (alreadyReserved) {
            throw new RoomEscapeException(DUPLICATE_RESERVATION);
        }
    }

    private static ReservationRequest fillMissingNameWithLoginMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (!StringUtils.hasText(reservationRequest.name())) {
            reservationRequest = reservationRequest.withDefaultName(loginMember.name());
        }
        return reservationRequest;
    }
}
