package roomescape.reservation.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WaitingRequest {
    private String date;
    private Long time;
    private Long theme;
}
