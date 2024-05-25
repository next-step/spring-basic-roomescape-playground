package roomescape.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WaitingWithRank {
    private Waiting waiting;
    private Long rank;
}