package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.ForbiddenException;
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

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {

        Member member = resolveReservationMember(request, loginMember);

        Reservation reservation = reservationDao.save(request, member);

        return new ReservationResponse(
                reservation.getId(),
                member.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    private Member resolveReservationMember(ReservationRequest request, LoginMember loginMember) {

        if (request.name() != null && request.name().isBlank()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }

        if (request.name() == null) {
            return memberService.findById(loginMember.id());
        }

        if (loginMember.role().equals("ADMIN")) {
            return memberService.findByName(request.name());
        }

        if (!loginMember.name().equals(request.name())) {
            throw new ForbiddenException("다른 사용자의 이름으로 예약할 수 없습니다.");
        }

        return memberService.findById(loginMember.id());
    }
}
