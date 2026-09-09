package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberService;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberService memberService;

    public ReservationService(ReservationDao reservationDao, MemberService memberService) {
        this.reservationDao = reservationDao;
        this.memberService = memberService;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member member = findReservationMember(reservationRequest, loginMember);
        Reservation reservation = reservationDao.save(
                reservationRequest.date(),
                member.getName(),
                reservationRequest.themeId(),
                reservationRequest.timeId()
        );

        return toResponse(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private Member findReservationMember(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (!loginMember.isAdmin() || reservationRequest.name() == null || reservationRequest.name().isBlank()) {
            return memberService.findById(loginMember.id());
        }
        return memberService.findByName(reservationRequest.name());
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }
}
