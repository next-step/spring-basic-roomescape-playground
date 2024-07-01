package roomescape.waiting;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingWithRank {
    private Waiting waiting;
    private Long rank;

}
