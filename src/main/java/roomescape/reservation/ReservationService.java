package roomescape.reservation;

import org.springframework.stereotype.Service;
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

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;


    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }
    public ReservationResponse save(ReservationRequest reservationRequest) {
        Time time = timeRepository.findByValue(reservationRequest.getTime()).orElseThrow(() -> new IllegalArgumentException("없는 시간입니다."));
        Theme theme = themeRepository.findByName(reservationRequest.getTheme()).orElseThrow(() -> new IllegalArgumentException("없는 테마입니다."));
        Member member = memberRepository.findByName(reservationRequest.getName()).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        validateDuplicate(reservationRequest, theme.getId(), time.getId());
        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme, member);
        reservationRepository.save(reservation);
        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationResponse> findAllByMemberName(String name) {
        return reservationRepository.findByName(name)
                .stream()
                .map(reservation -> new ReservationResponse(
                        reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue()
                )).collect(Collectors.toList());
    }

    private void validateDuplicate(ReservationRequest reservationRequest, Long themeId, Long timeId) {
        reservationRepository.findByDateAndThemeIdAndTimeId(reservationRequest, themeId, timeId)
                .ifPresent(it -> {
                    throw new IllegalArgumentException("이미 예약된 시간입니다.");
                });
    }

    public List<MyReservationResponse> findMyReservationsByMemberName(String name) {
        Member member = memberRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        final List<Reservation> reservations = reservationRepository.findByMemberId(member.getId());
        final List<WaitingWithRank> waitingWithRanks = waitingRepository.findWaitingsWithRankByMemberId(member.getId());

        return MyReservationResponse.of(reservations, waitingWithRanks);
    }
}
