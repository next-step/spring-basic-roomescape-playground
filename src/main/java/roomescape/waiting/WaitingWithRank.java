package roomescape.waiting;

public class WaitingWithRank {
    private final Waiting waiting;
    private final Long count; // COUNT 결과를 담을 필드 (Long 타입 필수)

    public WaitingWithRank(Waiting waiting, Long count) {
        this.waiting = waiting;
        this.count = count;
    }

    public Waiting getWaiting() {
        return waiting;
    }

    public Long getCount() {
        return count;
    }
}
