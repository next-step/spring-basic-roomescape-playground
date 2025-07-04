package roomescape.waiting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import roomescape.waiting.Waiting;

@Getter
@AllArgsConstructor
public class WaitingWithRank {

    private final Waiting waiting;
    private final Long rank;
}
