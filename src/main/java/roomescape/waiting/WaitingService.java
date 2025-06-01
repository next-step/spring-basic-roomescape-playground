package roomescape.waiting;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class WaitingService {

    private final WaitingRepository waitingRepository;

    public WaitingService(WaitingRepository waitingRepository) {
        this.waitingRepository = waitingRepository;
    }

    @Transactional
    public Long create(Long memberId, WaitingRequest waitingRequest) {
        Waiting waiting = new Waiting(
                new MemberId(memberId),
                new ThemeId(waitingRequest.getTheme()),
                new TimeId(waitingRequest.getTime()),
                waitingRequest.getDate()
        );

        if (waitingRepository.existWaiting(new MemberId(memberId), new ThemeId(waitingRequest.getTheme()), new TimeId(waitingRequest.getTime()))) {
            throw new WaitingAlreadyExistException();
        }

        waitingRepository.save(waiting);

        return waitingRepository.findMyRank(memberId, waitingRequest.getDate(), waitingRequest.getTheme(), waitingRequest.getTime());
    }

    @Transactional
    public void cancel(Long memberId, Long waitingId) {
        waitingRepository.delete(memberId, waitingId);
    }
}
