package roomescape.reservation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.InvalidDataException;
import roomescape.exception.NotFoundDataException;
import roomescape.member.LoginMember;
import roomescape.member.MemberDao;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationDao reservationDao;
    private final MemberDao memberDao;

    public ReservationService(ReservationDao reservationDao, MemberDao memberDao) {
        this.reservationDao = reservationDao;
        this.memberDao = memberDao;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest reservationRequest, LoginMember loginMember) {
        String reservationName = determineReservationName(reservationRequest, loginMember);

        ReservationRequest request = reservationRequest.getName() == null
                ? new ReservationRequest(reservationName, reservationRequest.getDate(), reservationRequest.getTheme(), reservationRequest.getTime())
                : reservationRequest;

        Reservation reservation = reservationDao.save(request);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private String determineReservationName(ReservationRequest request, LoginMember loginMember) {
        if (request.getName() != null && !request.getName().isBlank()) {
            try {
                memberDao.findByName(request.getName());
            } catch (NotFoundDataException e) {
                throw e;
            }
            return request.getName();
        }

        if (loginMember != null) {
            return loginMember.name();
        }

        throw new InvalidDataException("예약자 정보가 필요합니다.");
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
}
