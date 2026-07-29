package roomescape.reservation;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.LoginMember;
import roomescape.exception.ForbiddenException;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.member.Role;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberService memberService, ThemeRepository themeRepository, TimeRepository timeRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {

        Member member = resolveReservationMember(request, loginMember);

        validateReservation(request);

        Theme theme = themeRepository.findById(request.theme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));

        Time time = timeRepository.findById(request.time())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        Reservation reservation = new Reservation(
                member.getName(),
                request.date(),
                time,
                theme,
                member
        );

        try {
            reservationRepository.save(reservation);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("이미 예약된 일정입니다.");
        }

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {

        List<MyReservationResponse> reservations =
                reservationRepository.findByMemberId(loginMember.id())
                        .stream()
                        .map(MyReservationResponse::from)
                        .toList();

        List<MyReservationResponse> waitings =
                waitingRepository.findWaitingsWithRankByMemberId(loginMember.id())
                        .stream()
                        .map(MyReservationResponse::from)
                        .toList();

        List<MyReservationResponse> result = new ArrayList<>();

        result.addAll(reservations);
        result.addAll(waitings);

        return result;
    }

    private Member resolveReservationMember(ReservationRequest request, LoginMember loginMember) {

        if (request.name() != null && request.name().isBlank()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }

        if (request.name() == null) {
            return memberService.findById(loginMember.id());
        }

        if (loginMember.role() == Role.ADMIN) {
            return memberService.findByName(request.name());
        }

        if (!loginMember.name().equals(request.name())) {
            throw new ForbiddenException("다른 사용자의 이름으로 예약할 수 없습니다.");
        }

        return memberService.findById(loginMember.id());
    }

    private void validateReservation(ReservationRequest request) {

        if (reservationRepository.existsByDateAndTimeIdAndThemeId(
                request.date(),
                request.time(),
                request.theme())) {
            throw new IllegalArgumentException("이미 예약된 일정입니다.");
        }
    }
}
