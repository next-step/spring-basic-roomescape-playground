package roomescape.time;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

@Service
public class TimeService {
	private ReservationRepository reservationRepository;
	private TimeRepository timeRepository;

	public TimeService(TimeRepository timeRepository, ReservationRepository reservationRepository) {
		this.timeRepository = timeRepository;
		this.reservationRepository = reservationRepository;
	}

	@Transactional(readOnly = true)
	public List<AvailableTime> getAvailableTime(String date, Long themeId) {
		List<Reservation> reservations = reservationRepository.findByDateAndThemeId(date, themeId);
		List<Time> times = timeRepository.findAll();

		return times.stream()
			.map(time -> new AvailableTime(
				time.getId(),
				time.getValue(),
				reservations.stream()
					.anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
			))
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Time> findAll() {
		return timeRepository.findAllByDeletedFalse();
	}

	public Time save(Time time) {
		return timeRepository.save(time);
	}

	@Transactional
	public void deleteById(Long id) {
		Time time = timeRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException("Time not found with id: " + id));
		time.markAsDeleted();
	}

}
