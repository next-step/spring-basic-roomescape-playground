package roomescape.reservation;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import roomescape.auth.LoginMember;
import roomescape.exception.ForbiddenException;
import roomescape.member.Member;
import roomescape.member.MemberService;
import roomescape.reservation.dto.MyReservationResponse;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;

    public ReservationService(ReservationRepository reservationRepository, MemberService memberService) {
        this.reservationRepository = reservationRepository;
        this.memberService = memberService;
    }

    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {

        Member member = resolveReservationMember(request, loginMember);

        Reservation reservation = reservationRepository.save(request, member);

        return ReservationResponse.from(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll()
                .stream()
                .map(ReservationResponse::from)
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

    public List<MyReservationResponse> findMyReservations(LoginMember loginMember) {

        return reservationRepository.findByMemberId(loginMember.id())
                .stream()
                .map(MyReservationResponse::from)
                .toList();
    }
}
