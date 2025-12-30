package roomescape.service;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.dao.ReservationDao;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;
import roomescape.exception.BadRequestException;
import roomescape.model.Member;
import roomescape.model.Reservation;

@Service
public class ReservationService {
    private ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public ReservationResponse create(ReservationRequest request, Member member) {
        String name = resolveName(request, member);

        ReservationRequest finalized = new ReservationRequest(name, request.date(), request.theme(), request.time());

        Reservation reservation = reservationDao.save(finalized);

        return new ReservationResponse(reservation.getId(), reservation.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }

    private String resolveName(ReservationRequest request, Member member) {
        if (request.name() != null) return request.name();
        if (member != null) return member.getName();

        throw new BadRequestException("예약자 이름은 누락될 수 없습니다.");
    }
}
