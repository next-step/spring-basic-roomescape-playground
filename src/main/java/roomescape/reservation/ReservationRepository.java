package roomescape.reservation;

import java.util.List;

import org.springframework.data.repository.Repository;

import roomescape.theme.Theme;

public interface ReservationRepository extends Repository<Reservation, Long> {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<Reservation> findByDateAndTheme(String date, Theme theme);
}
