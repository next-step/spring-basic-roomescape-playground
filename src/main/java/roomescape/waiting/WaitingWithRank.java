package roomescape.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class WaitingWithRank {
    private Waiting waiting;
    private Long rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank;
    }
}
