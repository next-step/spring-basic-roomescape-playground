package roomescape.time;

import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParticipationTimeService {

    private final ParticipationTimeRepository participationTimeRepository;
    private final ReservationRepository reservationRepository;

    public ParticipationTimeService(final ParticipationTimeRepository participationTimeRepository, final ReservationRepository reservationRepository) {
        this.participationTimeRepository = participationTimeRepository;
        this.reservationRepository = reservationRepository;
    }


    public List<AvailableTime> getAvailableTime(String date, Long themeId) {

        List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date, themeId);
        List<ParticipationTime> participationTimes = participationTimeRepository.findAll();

        return participationTimes.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getTime(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<ParticipationTime> findAll() {
        return participationTimeRepository.findAll();
    }

    public ParticipationTime save(ParticipationTime participationTime) {

        Optional<ParticipationTime> existingTime = participationTimeRepository.findByTime(participationTime.getTime());

        if (existingTime.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 참여 시간입니다.");
        }

        return participationTimeRepository.save(participationTime);
    }

    public void deleteById(Long id) {
        if (reservationRepository.existsByParticipationTimeId(id)) {
            throw new IllegalArgumentException("해당 시간에 예약이 존재하여 삭제할 수 없습니다.");
        }
        participationTimeRepository.deleteById(id);

    }
}
