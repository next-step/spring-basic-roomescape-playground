package roomescape.reservation;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    Optional<Reservation> findById(Long id);

    List<Reservation> findByMemberId(Long memberId);

    List<Reservation> findAllByDateAndThemeId(String date, long themeId);

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean existsByParticipationTimeId(Long participationTimeId);

    boolean existsByThemeId(Long themeId);

}
