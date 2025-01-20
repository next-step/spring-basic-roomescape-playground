package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository,
                              MemberRepository memberRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeRepository.getById(reservationRequest.getTheme());
        Time time = timeRepository.getById(reservationRequest.getTime());
        Member member = memberRepository.findByName(reservationRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        validateExistence(reservationRequest, theme, time);

        Reservation reservation = new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                member);

        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(),
                reservationRequest.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue());
    }

    private void validateExistence(final ReservationRequest reservationRequest, final Theme theme, final Time time) {
        reservationRepository.findByDateAndThemeIdAndTimeId(reservationRequest.getDate(), theme.getId(), time.getId())
                .ifPresent(it -> {
                    throw new IllegalArgumentException("이미 예약된 시간입니다.");
                });
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservationsByName(String name) {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        final List<Reservation> reservations = reservationRepository.findByMemberId(member.getId());
        final List<WaitingWithRank> waitingWithRanks = waitingRepository.findWaitingsWithRankByMemberId(member.getId());
        return MyReservationResponse.of(reservations, waitingWithRanks);
    }
}
