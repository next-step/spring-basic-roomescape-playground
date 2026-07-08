package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndTheme_Id(String date, Long themeId);
    List<Reservation> findByMember_Id(Long memberId);
    boolean existsByDateAndTime_IdAndTheme_Id(String date, Long timeId, Long themeId);
    boolean existsByMember_IdAndDateAndTime_IdAndTheme_Id(Long memberId, String date, Long timeId, Long themeId);
}
