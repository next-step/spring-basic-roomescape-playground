package roomescape.reservation;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
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

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Member member = memberRepository.getReferenceByName(reservationRequest.name());
        Time time = timeRepository.getReferenceById(reservationRequest.time());
        Theme theme = themeRepository.getReferenceById(reservationRequest.theme());

        Reservation reservation = reservationRepository.save(
                new Reservation(member, reservationRequest.date(), time, theme));

        return new ReservationResponse(reservation.getId(), reservationRequest.name(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약입니다");
        }

        reservationRepository.deleteById(id);
        reservationRepository.flush();

        List<Waiting> waitings = waitingRepository.findByDateAndTimeAndTheme(reservation.get().getDate(),
                reservation.get().getTime(), reservation.get().getTheme());

        waitings.forEach(waiting -> {
            waiting.proceed();
            if (waiting.getOrder() <= 0) {
                waitingRepository.deleteById(waiting.getId());
                reservationRepository.save(
                        new Reservation(waiting.getMember(), waiting.getDate(), waiting.getTime(), waiting.getTheme()));
            }
        });
    }

    public List<MyReservationResponse> findMyReservations(Member member) {
        return reservationRepository.findByMemberId(member.getId()).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue(), "예약"))
                .toList();
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getMember().getName(), it.getTheme().getName(),
                        it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
