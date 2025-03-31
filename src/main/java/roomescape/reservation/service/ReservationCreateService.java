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
import roomescape.waiting.repository.WaitingRepository;

@Service
@Transactional
public class ReservationCreateService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationCreateService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                                    TimeRepository timeRepository, ThemeRepository themeRepository,
                                    WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
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

    private ReservationResponse processReservation(String date, Member member, Time time, Theme theme, LoginMember loginMember) {
        Reservation reservation = new Reservation(date, member, time, theme);
        
        validateReservationCreation(reservation);

        if (loginMember.isNotAdmin()) {
            return createReservation(reservation);
        }

        if (isAlreadyReserved(reservation)) {
            return createWaiting(date, member, time, theme, reservation);
        }

        reservationRepository.save(reservation);
        return ReservationResponse.from(reservation, Status.RESERVATION);
    }

    private Member findMember(String email, String name) {
        return memberRepository.findMemberByEmailAndName(email, name)
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

    private ReservationResponse createWaiting(String date, Member member, Time time, Theme theme,
                                              Reservation reservation) {
        Reservation savedReservation = reservationRepository.findByDateAndTimeIdAndThemeId(
                reservation.getDate(),
                reservation.getTime().getId(),
                reservation.getTheme().getId()
        ).orElseThrow(() -> new IllegalArgumentException(ErrorMessage.RESERVATION_NOT_FOUND.getMessage()));

        if (savedReservation.isSavedSameMember(member)) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_RESERVATION.getMessage());
        }

        if (isAlreadyInWaiting(reservation)) {
            throw new IllegalArgumentException(ErrorMessage.ALREADY_WAITING.getMessage());
        }

        Waiting waiting = new Waiting(date, time.getValue(), theme, member, savedReservation);
        Waiting savedWaiting = waitingRepository.save(waiting);

        return ReservationResponse.from(savedWaiting.getReservation(), Status.WAIT);
    }

    private boolean isAlreadyInWaiting(Reservation reservation) {
        return waitingRepository.existsByMemberEmailAndDateAndTimeAndThemeId(
                reservation.getMember().getEmail(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                reservation.getTheme().getId()
        );
    }
}
