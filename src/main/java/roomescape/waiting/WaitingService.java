package roomescape.waiting;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.MyReservationResponse;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class WaitingService {
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final WaitingRepository waitingRepository;
    private final ReservationRepository reservationRepository;

    public WaitingService(MemberRepository memberRepository, TimeRepository timeRepository,
                          ThemeRepository themeRepository, WaitingRepository waitingRepository,
                          ReservationRepository reservationRepository) {
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.waitingRepository = waitingRepository;
        this.reservationRepository = reservationRepository;
    }

    public WaitingResponse save(WaitingRequest waitingRequest) {
        Member member = memberRepository.findByName(waitingRequest.name());
        Time time = timeRepository.getReferenceById(waitingRequest.time());
        Theme theme = themeRepository.getReferenceById(waitingRequest.theme());
        Long order = waitingRepository.countByDateAndTimeAndTheme(waitingRequest.date(), time, theme) + 1;

        Waiting waiting = waitingRepository.save(new Waiting(member, waitingRequest.date(), time, theme, order));

        return new WaitingResponse(waiting.getId(), waiting.getTheme().getName(), waiting.getDate(),
                waiting.getTime().getValue(), waiting.getOrder());
    }

    public List<MyReservationResponse> findMyWaitings(Member member) {
        return waitingRepository.findByMemberId(member.getId()).stream()
                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(),
                        it.getTime().getValue(), it.getOrder() + "번째 예약대기"))
                .toList();
    }

    @Transactional
    public void deleteById(Long id) {
        Optional<Waiting> waiting = waitingRepository.findById(id);
        if (waiting.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 예약 대기입니다");
        }

        waitingRepository.deleteById(id);
        waitingRepository.flush();

        List<Waiting> waitings = waitingRepository.findByDateAndTimeAndThemeAndOrderAfter(waiting.get().getDate(),
                waiting.get().getTime(), waiting.get().getTheme(), waiting.get().getOrder());

        waitings.forEach(w -> {
            w.proceed();
            if (w.getOrder() <= 0) {
                waitingRepository.deleteById(w.getId());
                reservationRepository.save(
                        new Reservation(w.getMember(), w.getDate(), w.getTime(), w.getTheme()));
            }
        });
    }
}
