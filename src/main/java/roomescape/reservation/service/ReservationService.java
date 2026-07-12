package roomescape.reservation.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.domain.LoginMember;
import roomescape.exception.ApplicationException;
import roomescape.member.exception.MemberErrorCode;
import roomescape.member.repository.MemberDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.repository.ReservationDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private ReservationDao reservationDao;
    private MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String name = resolveName(reservationRequest, loginMember);

        Reservation reservation = reservationDao.save(reservationRequest, name);

        return new ReservationResponse(reservation.getId(), name, reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    @Transactional
    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    private String resolveName(ReservationRequest reservationRequest, LoginMember loginMember) {
        if (reservationRequest.getName() != null && !reservationRequest.getName().isBlank()) {
            memberDao.findByName(reservationRequest.getName())
                    .orElseThrow(() -> new ApplicationException(MemberErrorCode.MEMBER_NOT_FOUND));
            return reservationRequest.getName();
        }

        return loginMember.name();
    }
}
