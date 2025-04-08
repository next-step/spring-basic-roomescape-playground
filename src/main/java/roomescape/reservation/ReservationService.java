package roomescape.reservation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.Waiting;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.WaitingWithRank;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;
    private final WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                              ThemeRepository themeRepository, TimeRepository timeRepository,
                              WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse saveForAdmin(ReservationRequest request) {
        Member foundMember = memberRepository.findByName(request.getName())
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return saveReservationWithMember(request, foundMember);
    }

    public ReservationResponse saveForUser(ReservationRequest request, LoginMember loginMember) {
        Member foundMember = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return saveReservationWithMember(request, foundMember);
    }

    private ReservationResponse saveReservationWithMember(ReservationRequest request, Member member) {
        Theme foundTheme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new NoSuchElementException("Theme not found"));
        Time foundTime = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new NoSuchElementException("Time not found"));
        Reservation reservation = new Reservation(member.getName(), request.getDate(), foundTime, foundTheme, member);
        Reservation saved = reservationRepository.save(reservation);
        return toReservationResponse(saved);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithThemeAndTime()
                .stream()
                .map(this::toReservationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MyReservationResponse> findByMemberId(Long memberId) {
        List<MyReservationResponse> myReservationResponses = reservationRepository.findByMemberId(memberId)
                .stream()
                .map(this::toMyReservationResponse)
                .toList();

        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId)
                .stream()
                .map(this::toMyWaitingResponse)
                .toList();

        List<MyReservationResponse> combined = new ArrayList<>();
        combined.addAll(myReservationResponses);
        combined.addAll(waitings);
        return combined;
    }

    private MyReservationResponse toMyReservationResponse(Reservation reservation) {
        return new MyReservationResponse(reservation);
    }

    private ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(reservation);
    }

    private MyReservationResponse toMyWaitingResponse(WaitingWithRank waitingWithRank) {
        Waiting waiting = waitingWithRank.waiting();
        return new MyReservationResponse(
                waiting.getId(),
                waiting.getTheme().getName(),
                waiting.getDate(),
                LocalTime.parse(waiting.getTime().getValue()),
                (waitingWithRank.rank() + 1) + "번째 예약대기"
        );
    }
}
