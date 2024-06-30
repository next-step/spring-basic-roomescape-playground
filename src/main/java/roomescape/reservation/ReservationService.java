package roomescape.reservation;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.auth.LoginMember;

@Service
public class ReservationService {
	private ReservationDao reservationDao;

	public ReservationService(ReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}

	public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
		if (reservationRequest.getName() == null) {
			reservationRequest = new ReservationRequest(member.name(), reservationRequest.getDate(),
				reservationRequest.getTheme(), reservationRequest.getTime());
		}
		Reservation reservation = reservationDao.save(reservationRequest);

		return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
			reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
	}

	public void deleteById(Long id) {
		reservationDao.deleteById(id);
	}

	public List<ReservationResponse> findAll() {
		return reservationDao.findAll().stream()
			.map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
				it.getTime().getValue()))
			.toList();
	}
}
