package roomescape.waiting;

import lombok.AllArgsConstructor;
import lombok.Data;
import roomescape.theme.Theme;
import roomescape.time.Time;
@Data
@AllArgsConstructor
public class WaitingResponse {

    private Long id;
    private Theme theme;
    private Time time;
    private String date;
}
