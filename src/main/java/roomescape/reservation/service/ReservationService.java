package roomescape.reservation.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.auth.AuthorizationException;
import roomescape.member.domain.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;
import roomescape.waiting.WaitingWithRank;
import roomescape.waiting.repository.WaitingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private static final String DUPLICATE_RESERVATION_MESSAGE = "이미 예약된 날짜, 테마, 시간입니다.";

    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberService memberService,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResult save(ReservationCommand command, LoginMember loginMember) {
        validateDuplicateReservation(command);
        Time time = findTime(command.timeId());
        Theme theme = findTheme(command.themeId());
        Reservation reservation = createReservation(command, loginMember, time, theme);

        try {
            return toResult(reservationRepository.save(reservation));
        } catch (DataIntegrityViolationException exception) {
            throw new IllegalArgumentException(DUPLICATE_RESERVATION_MESSAGE, exception);
        }
    }

    private void validateDuplicateReservation(ReservationCommand command) {
        if (reservationRepository.existsByDateAndThemeIdAndTimeId(
                command.date(),
                command.themeId(),
                command.timeId()
        )) {
            throw new IllegalArgumentException(DUPLICATE_RESERVATION_MESSAGE);
        }
    }

    @Transactional
    public void deleteById(Long id, LoginMember loginMember) {
        if (!loginMember.isAdmin()) {
            throw new AuthorizationException("예약을 삭제할 권한이 없습니다.");
        }
        reservationRepository.deleteById(id);
    }

    public List<ReservationResult> findAll() {
        return reservationRepository.findAllWithThemeAndTime().stream()
                .map(this::toResult)
                .toList();
    }

    public List<MyReservationResult> findMine(LoginMember loginMember) {
        List<MyReservationResult> results = new ArrayList<>(reservationRepository
                .findByMember_IdOrderByIdAsc(loginMember.id()).stream()
                .map(this::toMyReservationResult)
                .toList());
        results.addAll(waitingRepository.findWaitingsWithRankByMemberId(loginMember.id()).stream()
                .map(this::toMyWaitingResult)
                .toList());
        return results;
    }

    private Reservation createReservation(ReservationCommand command,
                                          LoginMember loginMember,
                                          Time time,
                                          Theme theme) {
        if (loginMember.isAdmin() && command.name() != null && !command.name().isBlank()) {
            return Reservation.byName(command.name(), command.date(), time, theme);
        }

        Member member = findReservationMember(command, loginMember);
        return Reservation.byMember(member, command.date(), time, theme);
    }

    private Member findReservationMember(ReservationCommand command, LoginMember loginMember) {
        if (isReservationForAnotherMember(command, loginMember)) {
            return memberService.findById(command.memberId());
        }
        return memberService.findById(loginMember.id());
    }

    private boolean isReservationForAnotherMember(ReservationCommand command, LoginMember loginMember) {
        return loginMember.isAdmin() && command.memberId() != null;
    }

    private Time findTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 시간을 찾을 수 없습니다."));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("예약 테마를 찾을 수 없습니다."));
    }

    private ReservationResult toResult(Reservation reservation) {
        return new ReservationResult(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private MyReservationResult toMyReservationResult(Reservation reservation) {
        return MyReservationResult.reserved(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private MyReservationResult toMyWaitingResult(WaitingWithRank waitingWithRank) {
        return MyReservationResult.waiting(
                waitingWithRank.getId(),
                waitingWithRank.getTheme(),
                waitingWithRank.getDate(),
                waitingWithRank.getTime(),
                waitingWithRank.getRank() + 1
        );
    }
}
