package roomescape.reservation;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.waiting.WaitingResponse;
import roomescape.waiting.WaitingService;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberService memberService;
    private final WaitingService waitingService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
                              ThemeRepository themeRepository
        , MemberService memberService, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberService = memberService;
        this.waitingService = waitingService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Member member) {
        Time time = timeRepository.findById(reservationRequest.getTime())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 시간"));

        Theme theme = themeRepository.findById(reservationRequest.getTheme())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테마"));

        Reservation reservation = new Reservation(
            member,
            reservationRequest.getDate(),
            time,
            theme
        );
        reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), theme.getName(),
                                       reservation.getDate(),
                                       time.getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
            .map(it -> new ReservationResponse(it.getId(), it.getMemberName(), it.getTheme().getName(), it.getDate(),
                                               it.getTime().getValue()))
            .toList();
    }

    public List<MyReservationResponse> findAllMine(Long memberId) {
        List<MyReservationResponse> allOfMyReservation = new ArrayList<>();

        List<Reservation> allReservationByMemberId = findAllByMemberId(memberId);
        allOfMyReservation.addAll(allReservationByMemberId.stream()
                                      .map(MyReservationResponse::from)
                                      .toList());

        List<WaitingResponse> allWaitingByMemberId = waitingService.findAllByMemberId(memberId);
        allOfMyReservation.addAll(allWaitingByMemberId.stream()
                                      .map(MyReservationResponse::from)
                                      .toList());

        return allOfMyReservation;
    }

    private List<Reservation> findAllByMemberId(Long memberId) {
        return reservationRepository.findAllByMemberId(memberId);
    }

    public boolean existsByMemberAndDateAndTimeAndTheme(Member member, String date, Time time, Theme theme) {
        return reservationRepository.existsByMemberAndDateAndTimeAndTheme(member, date, time, theme);
    }

}
