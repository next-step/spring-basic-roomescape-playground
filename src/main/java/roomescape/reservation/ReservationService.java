package roomescape.reservation;



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
import roomescape.waiting.WaitingWithRank;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository,
                              ThemeRepository themeRepository,
                              TimeRepository timeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAllWithFetchJoin();
    }

    @Transactional
    public Reservation createReservation(ReservationRequest reservationRequest, Member loginMember) {
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        String finalName = (reservationRequest.getName() != null && !reservationRequest.getName().isBlank())
                ? reservationRequest.getName()
                : loginMember.getName();

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마입니다."));
        Time time = timeRepository.findById(reservationRequest.getTime())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간입니다."));

        Reservation reservation = new Reservation(finalName, reservationRequest.getDate(), time, theme, member);
        return reservationRepository.save(reservation);
    }

    // 예약 삭제
    @Transactional
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<MyReservationResponse> findMyReservations(Member loginMember) {
        List<MyReservationResponse> result = new ArrayList<>();

        List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.getId());
        for (Reservation r : reservations) {
            result.add(new MyReservationResponse(
                    r.getId(),
                    r.getTheme().getName(),
                    r.getDate(),
                    r.getTime().getValue(),
                    "예약"
            ));
        }

        List<WaitingWithRank> waitingsWithRank = waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId());
        for (WaitingWithRank wr : waitingsWithRank) {
            Waiting w = wr.getWaiting();
            long displayRank = wr.getRank() + 1;
            result.add(new MyReservationResponse(
                    w.getId(),
                    w.getTheme().getName(),
                    w.getDate(),
                    w.getTime().getValue(),
                    displayRank + "번째 예약대기"
            ));
        }

        return result;
    }
}
