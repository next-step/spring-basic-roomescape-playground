package roomescape.waiting;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WaitingService {
    private final WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    public Waiting createWaiting(Long memberId, String date, Long timeId, Long themeId) {
        if (date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("Date, time, and theme must be provided");
        }

        if (memberId == null) {
            memberId = 0L;
        }

        Waiting waiting = new Waiting(memberId, date, timeId, themeId);
        return waitingRepository.save(waiting);
    }

    public List<WaitingWithRank> getWaitingsWithRankByMemberId(Long memberId) {
        return waitingRepository.findWaitingsWithRankByMemberId(memberId)
                .stream()
                .map(wr -> new WaitingWithRank(wr.getWaiting(), (long) (wr.getRank().intValue() + 1)))
                .collect(Collectors.toList());
    }

    public void deleteWaiting(Long waitingId) {
        waitingRepository.deleteById(waitingId);
    }
}
