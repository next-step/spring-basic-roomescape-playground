package roomescape.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.MemberNotFoundException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.time())
                .orElseThrow(() -> new IllegalArgumentException("해당 시간이 존재하지 않습니다."));
        Theme theme = themeRepository.findById(reservationRequest.theme())
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
        Member member = memberRepository.findByName(reservationRequest.name())
                .orElseThrow(() -> new MemberNotFoundException("해당 사용자가 존재하지 않습니다."));

        checkReservationExist(reservationRequest, theme, time);

        Reservation reservation = new Reservation(reservationRequest.name(), reservationRequest.date(), time, theme, member);
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getTime());
    }

    private void checkReservationExist(ReservationRequest request, Theme theme, Time time) {
        reservationRepository.findByDateAndThemeIdAndTimeId(request.date(), theme.getId(), time.getId())
                .ifPresent(it -> {
                    throw new IllegalArgumentException("이미 예약된 시간입니다.");
                });
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getTime()))
                .toList();
    }

    public List<MyReservationResponse> findByMemberId(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        List<MyReservationResponse> reservations = reservationRepository.findById(id).stream()
                .map(MyReservationResponse::fromReservation)
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(member.getId()).stream()
                .map(MyReservationResponse::fromWaitingWithRank)
                .toList();

        return Stream.concat(reservations.stream(), waitings.stream())
                .collect(Collectors.toList());
    }
}
