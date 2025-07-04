package roomescape.time;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvailableTime {

    private Long timeId;
    private String time;
    private boolean booked;
}
