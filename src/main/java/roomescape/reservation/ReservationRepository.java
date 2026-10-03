package roomescape.reservation;

import org.springframework.data.repository.CrudRepository;
import roomescape.member.Member;

import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Override
    List<Reservation> findAll();

    List<Reservation> findByMemberId(Long memberId);

    List<Reservation> findByDateAndThemeId(String date, Long themeId);

    List<Reservation> member(Member member);

    boolean existsByDateAndTimeIdAndThemeId(String date, Long timeId, Long themeId);
}
