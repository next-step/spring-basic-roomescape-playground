package roomescape.reservation.waiting;

import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class WaitingDao {
    private final WaitingRepository waitingRepository;

    public List<Waiting> findByThemeIdAndDateAndTime(Long themeId, String date, String time) {
        return waitingRepository.findByThemeIdAndDateAndTime(themeId, date, time);
    }

    public List<WaitingWithRank> findWaitingsWithRankByMemberId(@Param("memberId") Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId);
    }

    public Waiting save(Waiting waiting) {
        return waitingRepository.save(waiting);
    }

    public Waiting findById(Long id) {
        return waitingRepository.findById(id).orElseThrow();
    }

    public void deleteById(Long id) {
        waitingRepository.deleteById(id);
    }
}
