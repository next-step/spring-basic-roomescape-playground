package roomescape.reservation;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.auth.LoginMember;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final TimeRepository timeRepository;
	private final ThemeRepository themeRepository;

	public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
		ThemeRepository themeRepository) {
		this.reservationRepository = reservationRepository;
		this.timeRepository = timeRepository;
		this.themeRepository = themeRepository;
	}

	public ReservationResponse save(ReservationRequest reservationRequest, LoginMember member) {
		if (reservationRequest.getName() == null) {
			reservationRequest = new ReservationRequest(member.name(), reservationRequest.getDate(),
				reservationRequest.getTheme(), reservationRequest.getTime());
		}

		Time time = timeRepository.findById(reservationRequest.getTime())
			.orElseThrow(() -> new IllegalArgumentException("Invalid time ID"));
		Theme theme = themeRepository.findById(reservationRequest.getTheme())
			.orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));

		Reservation reservation = reservationRepository.save(
			new Reservation(member.name(), reservationRequest.getDate(), time, theme));

		return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
			reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
	}

	public void deleteById(Long id) {
		reservationRepository.deleteById(id);
	}

	public List<ReservationResponse> findAll() {
		return reservationRepository.findAll().stream()
			.map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(),
				it.getTime().getValue()))
			.toList();
	}

	public List<MyReservationResponse> findAllMyReservations(LoginMember loginMember) {
		return reservationRepository.findByMemberId(loginMember.id())
			.stream()
			.map(reservation -> new MyReservationResponse(reservation.getId(),
				reservation.getTheme().getName(),
				reservation.getDate(),
				reservation.getTime().getValue(), "예약"))
			.toList();
	}
}
