package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository, MemberRepository memberRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public List<MyReservationResponse> readAllByMember(String email) {
        Member member = memberRepository.findByEmailOrThrow(email);
        List<MyReservationResponse> reservations = getMemberReservation(member);
        List<MyReservationResponse> waitings = getMemberWaiting(member);
        return List.copyOf(Stream.concat(reservations.stream(), waitings.stream()).toList());
    }

    private List<MyReservationResponse> getMemberReservation(Member member) {
        return waitingRepository.findAllWithRankByMemberId(member.getId()).stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    private List<MyReservationResponse> getMemberWaiting(Member member) {
        return waitingRepository.findAllWithRankByMemberId(member.getId()).stream()
                .map(MyReservationResponse::from)
                .toList();
    }

    public ReservationResponse save(ReservationRequest reservationRequest, String email) {
        Member member = memberRepository.findByEmailOrThrow(email);
        Time time = timeRepository.findByIdOrThrow(reservationRequest.time());
        Theme theme = themeRepository.findByIdOrThrow(reservationRequest.theme());

        Reservation reservation = new Reservation(
                Optional.ofNullable(reservationRequest.name()).orElse(member.getName()), reservationRequest.date(),
                time, theme, member);

        Reservation savedReservation = reservationRepository.save(reservation);

        return ReservationResponse.from(savedReservation);
    }
    
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
