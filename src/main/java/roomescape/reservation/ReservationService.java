package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Member foundMember = getMemberByName(reservationRequest);

        Reservation reservation = saveReservation(reservationRequest, foundMember);

        return toReservationResponse(reservation);
    }

    private Member getMemberByName(ReservationRequest reservationRequest) {
        return memberDao.findByName(reservationRequest.getName())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        Member foundMember = getMemberById(loginMember);

        Reservation reservation = saveReservation(reservationRequest, foundMember);

        return toReservationResponse(reservation);
    }

    private Member getMemberById(LoginMember loginMember) {
        return memberDao.findById(loginMember.id())
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    private Reservation saveReservation(ReservationRequest reservationRequest, Member member) {
        return reservationDao.save(reservationRequest.getDate(), member.getName(),
                reservationRequest.getTheme(), reservationRequest.getTime());
    }

    private ReservationResponse toReservationResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(this::toReservationResponse)
                .toList();
    }

}
