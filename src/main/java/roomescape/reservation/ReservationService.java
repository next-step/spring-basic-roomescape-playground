package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.dto.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
        ThemeRepository themeRepository, TimeRepository timeRepository,
        MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request) {
        Theme theme = themeRepository.findById(request.getTheme())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 테마입니다. id=" + request.getTheme()));
        Time time = timeRepository.findById(request.getTime())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 시간입니다. id=" + request.getTime()));

        Reservation reservation = new Reservation(
            request.getName(),
            request.getDate(),
            time,
            theme
        );

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(),
            saved.getTheme().getName(), saved.getDate(),
            saved.getTime().getTime());
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Theme theme = themeRepository.findById(request.getTheme())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 테마입니다. id=" + request.getTheme()));
        Time time = timeRepository.findById(request.getTime())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 시간입니다. id=" + request.getTime()));
        Member member = memberRepository.findById(loginMember.id())
            .orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 회원입니다. id=" + loginMember.id()));

        String name = request.getName();
        if (name == null || name.isBlank()) {
            name = loginMember.name();
        }

        Reservation reservation = new Reservation(name, request.getDate(), member, time, theme);
        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(saved.getId(), saved.getName(),
            saved.getTheme().getName(), saved.getDate(), saved.getTime().getTime());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(),
                it.getDate(), it.getTime().getTime()))
            .toList();
    }

    public List<MyReservationResponse> findReservationsByMemberId(Long memberId) {
        return reservationRepository.findByMemberId(memberId).stream()
            .map(MyReservationResponse::from)
            .toList();
    }
}
