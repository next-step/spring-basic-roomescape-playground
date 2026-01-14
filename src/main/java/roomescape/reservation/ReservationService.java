package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorMessage;
import roomescape.exception.InvalidDataException;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingService waitingService;
    private final ReservationValidator reservationValidator;

    public ReservationService(ReservationRepository reservationRepository,
            MemberService memberService,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            WaitingService waitingService,
            ReservationValidator reservationValidator) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingService = waitingService;
        this.reservationValidator = reservationValidator;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member = determineMember(reservationRequest, loginMember);

        Time time = timeRepository.findById(reservationRequest.time())
                                  .orElseThrow(() -> new NotFoundDataException(ErrorMessage.TIME_NOT_FOUND.getMessage()));

        Theme theme = themeRepository.findById(reservationRequest.theme())
                                     .orElseThrow(() -> new NotFoundDataException(ErrorMessage.THEME_NOT_FOUND.getMessage()));

        reservationValidator.validateReservationCreation(member.getId(), reservationRequest.date(), time.getId(), theme.getId());

        Reservation reservation = new Reservation(
                member.getName(),
                reservationRequest.date(),
                time,
                theme,
                member
        );

        reservationRepository.save(reservation);

        return toReservationResponse(reservation);
    }

    private Member determineMember(ReservationRequest request, LoginMember loginMember) {
        if (request.name() != null && !request.name().isBlank()) {
            return memberService.findByName(request.name());
        }

        if (loginMember != null) {
            return memberService.findById(loginMember.id());
        }

        throw new InvalidDataException(ErrorMessage.MEMBER_INFO_REQUIRED.getMessage());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithRelations().stream()
                                    .map(this::toReservationResponse)
                                    .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {
        Stream<MyReservationResponse> reservations = reservationRepository.findByMemberId(loginMember.id())
                .stream()
                .map(this::toMyReservationResponse);

        Stream<MyReservationResponse> waitings = waitingService.findWaitingsWithRankByMemberId(loginMember.id())
                .stream()
                .map(this::toMyWaitingResponse);

        return Stream.concat(reservations, waitings).toList();
    }

    private ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private MyReservationResponse toMyReservationResponse(Reservation reservation) {
        return new MyReservationResponse(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                "예약"
        );
    }

    private MyReservationResponse toMyWaitingResponse(WaitingWithRank waitingWithRank) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getDate(),
                waitingWithRank.getWaiting().getTime().getValue(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }
}
