package roomescape.waiting.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WaitingWithRank {

    private Waiting waiting;
    private Long rank;
}
