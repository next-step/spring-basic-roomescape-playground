package roomescape.domain.reservation.repository;

import roomescape.domain.reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(String name, LocalDate date, Long themeId, Long timeId);

    void deleteById(Long reservationId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

}
