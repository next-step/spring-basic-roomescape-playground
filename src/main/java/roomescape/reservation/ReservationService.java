package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;
import roomescape.member.MemberRepository;
import roomescape.member.Member;
import roomescape.waiting.WaitingService;
import roomescape.waiting.WaitingWithRank;

import java.util.List;
import java.util.ArrayList;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final MemberRepository memberRepository;
    private final WaitingService waitingService;

    public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository, ThemeRepository themeRepository, MemberRepository memberRepository, WaitingService waitingService) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.memberRepository = memberRepository;
        this.waitingService = waitingService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, Long loginMemberId) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow();
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow();
        Reservation reservation = new Reservation(reservationRequest.getName(), reservationRequest.getDate(), time, theme);
        if (loginMemberId != null) {
            Member memberRef = memberRepository.getReferenceById(loginMemberId);
            reservation.setMember(memberRef);
        }
        reservation = reservationRepository.save(reservation);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }


}
