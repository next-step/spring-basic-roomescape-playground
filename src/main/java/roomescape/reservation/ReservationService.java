package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final WaitingRepository waitingRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              WaitingRepository waitingRepository,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository,
                              MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeRepository.getById(reservationRequest.theme());
        Time time = timeRepository.getById(reservationRequest.time());
        Member member = memberRepository.findByName(reservationRequest.name())
                                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        validateExistence(reservationRequest, theme, time);

        Reservation reservation = new Reservation(reservationRequest.name(),
                                                  reservationRequest.date(),
                                                  time,
                                                  theme,
                                                  member);

        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.name(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private void validateExistence(final ReservationRequest reservationRequest, final Theme theme, final Time time) {
        reservationRepository.findByDateAndThemeIdAndTimeId(reservationRequest.date(), theme.getId(), time.getId())
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
