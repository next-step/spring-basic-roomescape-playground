package roomescape.reservation.service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.LoginMember;
import roomescape.error.ErrorMessage;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.Status;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;
import roomescape.waiting.repository.WaitingRepository;
import roomescape.waiting.service.WaitingService;

@Service
@Transactional
public class ReservationCreateService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;
    private final WaitingService waitingService;

    public ReservationCreateService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                                    TimeRepository timeRepository, ThemeRepository themeRepository,
                                    WaitingRepository waitingRepository, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
        this.waitingService = waitingService;
    }

    public ReservationResponse saveUserReservation(ReservationRequest request, LoginMember loginMember) {
        return processReservation(request.getDate(),
                findMember(loginMember.email(), loginMember.name()),
                findTime(request.getTime()),
                findTheme(request.getTheme()),
                loginMember
        );
    }

    public ReservationResponse saveAdminReservation(AdminReservationRequest request, LoginMember loginMember) {
        return processReservation(request.getDate(),
                findMember(request.getEmail(), request.getName()),
                findTime(request.getTime()),
                findTheme(request.getTheme()),
                loginMember
        );
    }

    private Member findMember(String email, String name) {
        return memberRepository.findByEmailAndName(email, name)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Time findTime(Long timeId) {
        return timeRepository.findById(timeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
    }

    private Theme findTheme(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));
    }

    private ReservationResponse processReservation(String date, Member member, Time time, Theme theme, LoginMember loginMember) {
        Reservation reservation = new Reservation(date, member, time, theme);
        validateReservationCreation(reservation);

        if (loginMember.isNotAdmin()) {
            return createReservation(reservation);
        }
        if (isAlreadyReserved(reservation)) {
            WaitingRequest waitingRequest = new WaitingRequest(date, time.getId(), theme.getId());
            WaitingResponse waitingResponse = waitingService.createWaiting(waitingRequest, member, loginMember);
            Waiting waiting = findWaiting(waitingResponse);

            return ReservationResponse.from(waiting.getReservation(), waiting, Status.WAIT);
        }
        reservationRepository.save(reservation);

        return ReservationResponse.from(reservation, Status.RESERVATION);
    }

    private void validateReservationCreation(Reservation reservation) {
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessage.RESERVATION_MUST_AFTER_NOW.getMessage());
        }
    }

    private boolean isAlreadyReserved(Reservation reservation) {
        return reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        );
    }

    private ReservationResponse createReservation(Reservation reservation) {
        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation);
    }

    private Waiting findWaiting(WaitingResponse waitingResponse) {
        return waitingRepository.findById(waitingResponse.getWaitingId())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.WAITING_NOT_FOUND.getMessage()));
    }
}
