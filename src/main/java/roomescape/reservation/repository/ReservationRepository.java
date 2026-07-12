package roomescape.reservation.repository;

import roomescape.reservation.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    List<Reservation> findAllByMemberId(Long memerId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);

    Reservation save(Reservation reservation);

    void deleteById(Long id);
}
