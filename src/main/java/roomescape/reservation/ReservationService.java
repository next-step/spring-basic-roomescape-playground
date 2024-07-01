package roomescape.reservation;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import roomescape.auth.LoginMember;
import roomescape.reservation.waiting.Waiting;
import roomescape.reservation.waiting.WaitingRepository;
import roomescape.reservation.waiting.WaitingResponse;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final TimeRepository timeRepository;
	private final ThemeRepository themeRepository;
	private final WaitingRepository waitingRepository;

	public ReservationService(ReservationRepository reservationRepository, TimeRepository timeRepository,
		ThemeRepository themeRepository, WaitingRepository waitingRepository) {
		this.reservationRepository = reservationRepository;
		this.timeRepository = timeRepository;
		this.themeRepository = themeRepository;
		this.waitingRepository = waitingRepository;
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

		boolean reservationExists = reservationRepository.existsByDateAndTimeAndTheme(reservationRequest.getDate(),
			time, theme);

		if (reservationExists) {
			return new ReservationResponse(null, reservationRequest.getName(), theme.getName(),
				reservationRequest.getDate(), time.getValue());
		}

		Reservation reservation = reservationRepository.save(
			new Reservation(member.name(), reservationRequest.getDate(), time, theme));

		return new ReservationResponse(reservation.getId(), reservationRequest.getName(),
			reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
	}

	public WaitingResponse wait(ReservationRequest reservationRequest, LoginMember member) {
		if (reservationRequest.getName() == null) {
			reservationRequest = new ReservationRequest(member.name(), reservationRequest.getDate(),
				reservationRequest.getTheme(), reservationRequest.getTime());
		}

		Time time = timeRepository.findById(reservationRequest.getTime())
			.orElseThrow(() -> new IllegalArgumentException("Invalid time ID"));
		Theme theme = themeRepository.findById(reservationRequest.getTheme())
			.orElseThrow(() -> new IllegalArgumentException("Invalid theme ID"));

		Waiting waiting = waitingRepository.save(
			new Waiting(member.name(), reservationRequest.getDate(), time, theme));

		return new WaitingResponse(waiting.getId(), reservationRequest.getName(),
			waiting.getTheme().getName(), waiting.getDate(), waiting.getTime().getValue());
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
		List<Reservation> reservations = reservationRepository.findByMemberId(loginMember.id());
		List<MyReservationResponse> reservationResponses = reservations.stream()
			.map(reservation -> new MyReservationResponse(
				reservation.getId(),
				reservation.getTheme().getName(),
				reservation.getDate(),
				reservation.getTime().getValue(),
				"예약"
			))
			.collect(Collectors.toList());

		List<Waiting> waitings = waitingRepository.findByName(loginMember.name());
		List<MyReservationResponse> waitingResponses = waitings.stream()
			.map(waiting -> new MyReservationResponse(
				waiting.getId(),
				waiting.getTheme().getName(),
				waiting.getDate(),
				waiting.getTime().getValue(),
				getWaitingRank(waiting) + "번째 예약대기"
			))
			.collect(Collectors.toList());

		reservationResponses.addAll(waitingResponses);
		return reservationResponses;
	}

	private int getWaitingRank(Waiting waiting) {
		List<Waiting> allWaitings = waitingRepository.findAllByDateAndTimeAndThemeOrderByDateAscTimeAsc(
			waiting.getDate(), waiting.getTime(), waiting.getTheme());
		return IntStream.range(0, allWaitings.size())
			.filter(i -> allWaitings.get(i).getId().equals(waiting.getId()))
			.findFirst()
			.orElse(-1) + 1;
	}

}
