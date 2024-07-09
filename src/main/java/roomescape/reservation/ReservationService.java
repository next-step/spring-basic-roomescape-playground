package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.exception.NotFoundException;
import roomescape.member.dto.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingRepository;
import roomescape.waiting.dto.WaitingWithRank;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ReservationService {
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;
    private WaitingRepository waitingRepository;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository, WaitingRepository waitingRepository) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingRepository = waitingRepository;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {

        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow(RuntimeException::new);
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow(RuntimeException::new);
        Member member = memberRepository.findById(reservationRequest.getMember()).orElseThrow(RuntimeException::new);

        Reservation reservation = reservationRepository.save(new Reservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                time,
                theme,
                member
        ));

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

    public List<MyReservationResponse> findAllByMember(LoginMember loginMember) {
        List<MyReservationResponse> responseList =
                reservationRepository.findByMemberId(loginMember.getId())
                .stream()
                .map(reservation -> new MyReservationResponse(
                        reservation.getId(),
                        reservation.getTheme().getName(),
                        reservation.getTime().getValue(),
                        reservation.getDate(),
                        "예약"))
                .toList();

        List<MyReservationResponse> waitingList =
                waitingRepository.findWaitingsWithRankByMemberId(loginMember.getId()).stream()
                        .map(this::covertWaitingWithRankToMyReservationResponse)
                        .toList();

        return Stream.concat(responseList.stream(), waitingList.stream())
                .sorted(Comparator.comparing(MyReservationResponse::getDate))
                .toList();
    }

    private MyReservationResponse covertWaitingWithRankToMyReservationResponse(
            WaitingWithRank waitingWithRank
    ) {
        return new MyReservationResponse(
                waitingWithRank.getWaiting().getId(),
                waitingWithRank.getWaiting().getTheme().getName(),
                waitingWithRank.getWaiting().getTime().getValue(),
                waitingWithRank.getWaiting().getDate(),
                (waitingWithRank.getRank() + 1) + "번째 예약대기"
        );
    }
}
