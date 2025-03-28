package roomescape.reservation.admin;

import java.util.List;
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

@Service
public class AdminReservationService {
    private final MemberRepository memberRepository;
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public AdminReservationService(MemberRepository memberRepository, ReservationRepository reservationRepository,
                                   TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.memberRepository = memberRepository;
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public AdminReservationResponse saveAdminReservation(AdminReservationRequest adminReservationRequest) {
        Member member = findMemberByEmail(adminReservationRequest);
        Time time = findTimeById(adminReservationRequest);
        Theme theme = findThemeById(adminReservationRequest);

        Reservation reservation = new Reservation(adminReservationRequest.getDate(), member, time, theme);

        return saveReservation(reservation, adminReservationRequest);
    }

    private Member findMemberByEmail(AdminReservationRequest adminReservationRequest) {
        return memberRepository.findMemberByEmail(adminReservationRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    private Time findTimeById(AdminReservationRequest adminReservationRequest) {
        return timeRepository.findById(adminReservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.TIME_NOT_FOUND.getMessage()));
    }

    private Theme findThemeById(AdminReservationRequest adminReservationRequest) {
        return themeRepository.findById(adminReservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.THEME_NOT_FOUND.getMessage()));
    }

    private AdminReservationResponse saveReservation(Reservation reservation, AdminReservationRequest adminReservationRequest) {
        Reservation savedReservation = reservationRepository.save(reservation);

        return new AdminReservationResponse(savedReservation.getId(), adminReservationRequest.getName(), adminReservationRequest.getEmail(),
                savedReservation.getTheme().getName(), savedReservation.getDate(),
                savedReservation.getTime().getValue());
    }

    public List<AdminReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(reservation -> new AdminReservationResponse(reservation.getId(), reservation.getMember().getName(), reservation.getMember().getEmail(),
                        reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue()))
                .toList();
    }
}
