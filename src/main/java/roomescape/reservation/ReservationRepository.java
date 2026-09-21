package roomescape.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByDateAndTheme_Id(String date, Long themeId);
    List<Reservation> findByMember_Id(Long memberId);
    // 해당 날짜, 테마, 시간에 내가 이미 예약했는지 확인
    boolean existsByDateAndTheme_IdAndTime_IdAndMember_Id(String date, Long themeId, Long timeId, Long memberId);
    // 해당 날짜·테마·시간에 (누구든) 이미 예약이 있는지 확인
    boolean existsByDateAndTheme_IdAndTime_Id(String date, Long themeId, Long timeId);
}
