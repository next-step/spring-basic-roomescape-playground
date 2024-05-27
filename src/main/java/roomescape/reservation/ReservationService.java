package roomescape.reservation;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private ReservationRepository reservationRepository;
    private MemberRepository memberRepository;
    @Autowired
    private WaitingRepository waitingRepository;
    @Autowired
    private MemberService memberService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Long memberId) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElse(null);
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElse(null);
        Member member = memberRepository.findById(memberId).orElse(null);

        Reservation reservation = Reservation.builder()
                .name(reservationRequest.getName())
                .date(reservationRequest.getDate())
                .theme(theme)
                .time(time)
                .member(member)
                .build();
        Reservation savedReservation = reservationRepository.save(reservation);
        return new ReservationResponse(savedReservation.getId(), savedReservation.getName(), savedReservation.getTheme().getName(), savedReservation.getDate(), savedReservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<MyReservationResponse> findMyReservation(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String token=memberService.extractTokenFromCookie(cookies); // 쿠키에서 토큰 추출
        Member member = memberService.findByToken(token); // 추출한 토큰으로 멤버 찾기
        Long memberId = member.getId(); // 내 아이디
        List<Reservation> reservationList = reservationRepository.findByMemberId(memberId);
        List<MyReservationResponse> myReservationResponseList = new ArrayList<>();
        for(Reservation reservation : reservationList) {
            MyReservationResponse myReservationResponse = MyReservationResponse.builder()
                    .reservationId(reservation.getId())
                    .theme(reservation.getTheme().getName())
                    .date(reservation.getDate())
                    .time(reservation.getTime().getValue())
                    .status("예약")
                    .build();
            myReservationResponseList.add(myReservationResponse);
        }

//        List<MyReservationResponse> reservations = reservationRepository.findByMemberId(memberResponse.getId()).stream()
//                .map(it -> new MyReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), "예약"))
//                .toList();
//
        List<MyReservationResponse> waitings = waitingRepository.findWaitingsWithRankByMemberId(memberId).stream()
                .map(it -> new MyReservationResponse(it.getWaiting().getId(), it.getWaiting().getTheme().getName(), it.getWaiting().getDate(), it.getWaiting().getTime().getValue(), (it.getRank() + 1) + "번째 예약대기"))
                .toList();

        List<MyReservationResponse> results = Stream.concat(myReservationResponseList.stream(), waitings.stream())
                .sorted(Comparator.comparing(MyReservationResponse::getDate))
                .toList();
        return myReservationResponseList;
    }
}
