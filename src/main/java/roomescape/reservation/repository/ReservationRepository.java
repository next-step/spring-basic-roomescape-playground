package roomescape.reservation.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import roomescape.member.model.Member;
import roomescape.reservation.model.Reservation;
import roomescape.theme.model.Theme;
import roomescape.time.model.Time;

public interface ReservationRepository extends Repository<Reservation, Long> {

    List<Reservation> findAll();

    List<Reservation> findAllByMember(Member member);

    Optional<Reservation> findById(Long id);

    Reservation save(Reservation reservation);

    void delete(Reservation reservation);

    List<Reservation> findByDateAndTheme(String date, Theme theme);

    Optional<Reservation> findByThemeAndDateAndTime(Theme theme, String date, Time time);

    default void checkDuplication(Theme theme, String date, Time time) {
        findByThemeAndDateAndTime(theme, date, time).ifPresent(reservation -> {
            throw new RuntimeException();
        });
    }
}
