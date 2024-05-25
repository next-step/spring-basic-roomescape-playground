package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;
import roomescape.member.Member;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Service
public class ReservationService {
    private MemberService memberService;
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberService memberService, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public ReservationResponse save(MemberResponse memberResponse, ReservationRequest reservationRequest) {
        Time time = timeRepository.findById(reservationRequest.getTime()).orElseThrow(RuntimeException::new);
        Theme theme = themeRepository.findById(reservationRequest.getTheme()).orElseThrow(RuntimeException::new);
        String name = reservationRequest.getName() == null ? findMemberName(memberResponse) : reservationRequest.getName();
        Member member = memberService.findMemberById(memberResponse.getId());

        Reservation reservation = reservationRepository.save(new Reservation(member, name, reservationRequest.getDate(), time, theme));

        return new ReservationResponse(reservation.getId(), reservation.getName() != null ? reservation.getName() : member.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    private String findMemberName(MemberResponse loginMember) {
        return memberService.findById(loginMember.getId()).getName();
    }


    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    public List<ReservationResponse> findMine(MemberResponse memberResponse) {
        List<ReservationResponse> reservations = reservationRepository.findByMemberId(memberResponse.getId()).stream()
                .map(it -> new ReservationResponse(it.getId(), it.getTheme().getName(), it.getDate(), it.getTime().getValue(), "예약"))
                .toList();
        return reservations;
    }
}
