package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.authentication.MemberAuthInfo;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.MemberNotFoundException;
import roomescape.exception.ThemeNotFoundException;
import roomescape.exception.TimeNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationResponse save(ReservationRequest reservationRequest, MemberAuthInfo memberAuthInfo) {
        validateReservationRequest(reservationRequest);

        if (reservationRequest.name() == null) {
            reservationRequest = new ReservationRequest(
                    memberAuthInfo.name(),
                    reservationRequest.date(),
                    reservationRequest.theme(),
                    reservationRequest.time());
        }

        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new ThemeNotFoundException("해당 테마를 찾을 수 없습니다."));
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new TimeNotFoundException("예약 시간을 찾을 수 없습니다."));

        Member member = null;
        if (memberAuthInfo.id() != null) { //관리자가 아닌, 사용자일 경우
            member = memberRepository.findById(memberAuthInfo.id())
                    .orElseThrow(() -> new MemberNotFoundException("가입된 회원이 아닙니다."));
        }

        validateDuplicateReservation(reservationRequest);

        Reservation reservation = new Reservation (
                reservationRequest.name(),
                reservationRequest.date(),
                time,
                theme,
                member
        );

        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(MemberAuthInfo memberAuthInfo) {

        List<ReservationResponse> reservationResponses = findAllByMemberName(memberAuthInfo.name());
        List<MyReservationResponse> myReservationResponses1 = reservationResponses
                .stream()
                .map((it -> new MyReservationResponse(
                        it.id(),
                        it.theme(),
                        it.date(),
                        it.time(),
                        "예약")))
                .collect(Collectors.toList());

        List<WaitingWithRank> waitingWithRanks = waitingRepository.findWaitingsWithRankByMemberId(memberAuthInfo.id());
        List<MyReservationResponse> myReservationResponses2 = waitingWithRanks.stream()
                .map((it -> new MyReservationResponse(
                        it.getWaiting().getId(),
                        it.getWaiting().getTheme().getName(),
                        it.getWaiting().getDate(),
                        it.getWaiting().getTime().getValue(),
                        it.getRank()+ 1 + "번째 예약대기")))
                .collect(Collectors.toList());

        return Stream.concat(myReservationResponses1.stream(), myReservationResponses2.stream())
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    private List<ReservationResponse> findAllByMemberName(String name) {
        return reservationRepository.findByName(name).stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();

    }

    private void validateReservationRequest(ReservationRequest reservationRequest) {
        if (reservationRequest.date() == null || reservationRequest.theme() == null || reservationRequest.time() == null) {
            throw new IllegalArgumentException("유효하지 않은 요청입니다. 필요한 값이 누락되었습니다.");
        }
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest) {
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(
                reservationRequest.date(),
                reservationRequest.time(),
                reservationRequest.theme()
        );

        if (exists) {
            throw new DuplicateReservationException("이미 예약이 존재합니다.");
        }
    }
}
