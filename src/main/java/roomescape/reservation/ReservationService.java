package roomescape.reservation;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        Member foundMember = Optional.ofNullable(request.getName())
                .map(memberRepository::findByName)
                .orElseGet(() -> memberRepository.findById(loginMember.id()))
                .orElseThrow(() -> new NoSuchElementException("Member not found"));
        return saveReservationWithMember(request, foundMember);
    }

    private ReservationResponse saveReservationWithMember(ReservationRequest request, Member member) {
        Theme foundTheme = themeRepository.getById(request.getThemeId());
        Time foundTime = timeRepository.getById(request.getTimeId());
        Reservation reservation = new Reservation(member.getName(), request.getDate(), foundTime, foundTheme, member);
        Reservation saved = reservationRepository.save(reservation);
        return new ReservationResponse(saved);
    }

    @Transactional
    public void deleteById(Long id, LoginMember loginMember) {
        Reservation findReservation = reservationRepository.findByIdAndMemberId(id, loginMember.id())
                .orElseThrow(() -> new SecurityException("Unauthorized access"));
        reservationRepository.delete(findReservation);

        Theme theme = findReservation.getTheme();
        String date = findReservation.getDate();
        Time time = findReservation.getTime();

        waitingRepository.findFirstByThemeAndTimeAndDateOrderById(theme, time, date)
                .ifPresent(waiting -> {
                    Reservation reservation = new Reservation(waiting.getMember().getName(), waiting.getDate(),
                            waiting.getTime(), waiting.getTheme(), waiting.getMember());
                    reservationRepository.save(reservation);
                    waitingRepository.delete(waiting);
                });
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllWithThemeAndTime()
                .stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MyReservationResponse> findByMemberId(Long memberId) {
        List<MyReservationResponse> myReservationResponses = reservationRepository.findByMemberId(memberId)
                .stream()
                .map(MyReservationResponse::new)
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
