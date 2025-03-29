package roomescape.reservation.admin;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import roomescape.error.ErrorMessage;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;

@Service
@Transactional
public class AdminReservationService {
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;

    public AdminReservationService(MemberRepository memberRepository, ReservationRepository reservationRepository,
                                   TimeRepository timeRepository, ThemeRepository themeRepository,
                                   WaitingRepository waitingRepository) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
    }

    public AdminReservationResponse saveAdminReservation(AdminReservationRequest adminReservationRequest) {
        Member member = findMember(adminReservationRequest);
        Time time = findTime(adminReservationRequest);
        Theme theme = findTheme(adminReservationRequest);

        Reservation reservation = new Reservation(adminReservationRequest.getDate(), member, time, theme);

        validateReservationCreation(reservation);

        Reservation foundReservation = reservationRepository.findByDateAndTimeIdAndThemeId(
                reservation.getDate(), reservation.getTime().getId(),
                reservation.getTheme().getId());

        if (foundReservation != null) {
            Waiting waiting = new Waiting(adminReservationRequest.getDate(), time.getValue(), theme, member, foundReservation);

            validateWaiting(waiting);

            Waiting savedWaiting = waitingRepository.save(waiting);

            return new AdminReservationResponse(savedWaiting.getReservation().getId(), savedWaiting.getMember().getName(),
                    savedWaiting.getMember().getEmail(), savedWaiting.getTheme().getName(), savedWaiting.getDate(),
                    savedWaiting.getTime());
        }

        return saveReservation(reservation, adminReservationRequest);
    }

    public List<AdminReservationResponse> findAll() {
        List<AdminReservationResponse> reservations = findUserReservations();
        List<AdminReservationResponse> waitings = findUserWaitings();

        return mergeAndSortReservationsByDate(reservations, waitings);
    }

    private Member findMember(AdminReservationRequest adminReservationRequest) {
        return memberRepository.findMemberByEmailAndName(adminReservationRequest.getEmail(), adminReservationRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Time findTime(AdminReservationRequest adminReservationRequest) {
        return timeRepository.findById(adminReservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
    }

    private Theme findTheme(AdminReservationRequest adminReservationRequest) {
        return themeRepository.findById(adminReservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));
    }

    private void validateReservationCreation(Reservation reservation) {
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessage.RESERVATION_MUST_AFTER_NOW.getMessage());
        }
    }

    private void validateWaiting(Waiting waiting) {
        if (waiting.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(ErrorMessage.WAITING_MUST_AFTER_NOW.getMessage());
        }
    }

    private AdminReservationResponse saveReservation(Reservation reservation, AdminReservationRequest adminReservationRequest) {
        Reservation savedReservation = reservationRepository.save(reservation);

        return new AdminReservationResponse(savedReservation.getId(), adminReservationRequest.getName(), adminReservationRequest.getEmail(),
                savedReservation.getTheme().getName(), savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }

    private List<AdminReservationResponse> findUserReservations() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new AdminReservationResponse(reservation.getId(), reservation.getMember().getName(),
                        reservation.getMember().getEmail(), reservation.getTheme().getName(), reservation.getDate(),
                        reservation.getTime().getValue()))
                .toList();
    }

    private List<AdminReservationResponse> findUserWaitings() {
        return waitingRepository.findAll().stream()
                .map(waiting -> new AdminReservationResponse(waiting.getReservation().getId(), waiting.getMember().getName(),
                            waiting.getMember().getEmail(), waiting.getTheme().getName(), waiting.getDate(),
                            waiting.getTime()))
                .toList();
    }

    private List<AdminReservationResponse> mergeAndSortReservationsByDate(List<AdminReservationResponse> reservations,
                                                                         List<AdminReservationResponse> waitings) {
        return Stream.concat(reservations.stream(), waitings.stream())
                .sorted(Comparator.comparing(AdminReservationResponse::getDate))
                .toList();
    }
}
