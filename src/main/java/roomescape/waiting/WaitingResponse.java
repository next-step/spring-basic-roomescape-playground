package roomescape.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WaitingResponse {
    private Long id;
    private Long theme;
    private String date;
    private String time;
    private int waitingNumber;
}