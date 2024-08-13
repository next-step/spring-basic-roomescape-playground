package roomescape.reservation;

import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingWithRank;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private WaitingRepository waitingRepository;
    private MemberRepository memberRepository;

    public ReservationService(ReservationRepository reservationRepository, WaitingRepository waitingRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.waitingRepository = waitingRepository;
        this.memberRepository = memberRepository;
    }

    private List<WaitingResponse> waitingList = new ArrayList<>();

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }

    public List<MyReservationResponse> findMyReservations(String memberName) {
        Member member = memberRepository.findMemberByName(memberName);
        return findMyReservationsById(member.getId());
    }
    public List<MyReservationResponse> findMyReservationsById(Long memberId) {
        List<WaitingWithRank> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId);

        return waitings.stream()
                .map(waitingWithRank -> new MyReservationResponse(
                        (long) waitingWithRank.getWaiting().getId(),
                        waitingWithRank.getWaiting().getTheme(),
                        waitingWithRank.getWaiting().getDate(),
                        waitingWithRank.getWaiting().getTime(),
                        waitingWithRank.getRank()+1 + "번째 예약대기"))
                .collect(Collectors.toList());
    }

    public WaitingResponse createWaiting(String date, String time, String theme, Long memberId) {
        Waiting waiting = new Waiting();
        waiting.setDate(date);
        waiting.setTime(time);
        waiting.setTheme(theme);
        waiting.setMemberId(memberId);

        Waiting savedWaiting = waitingRepository.save(waiting);

        Long rank = waitingRepository.countByThemeAndDateAndTimeAndIdLessThan(theme, date, time, savedWaiting.getId());

        return new WaitingResponse(
                savedWaiting.getId(),
                savedWaiting.getTheme(),
                savedWaiting.getDate(),
                savedWaiting.getTime(),
                (rank + 1) + "번째 예약대기"
        );
    }
}
