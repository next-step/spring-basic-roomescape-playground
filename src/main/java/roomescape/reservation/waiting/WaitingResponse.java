package roomescape.reservation.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WaitingResponse {
    private Long id;
    private String date;
    private String time; //time id
    private Long theme; //theme id
    private int waitingNum;
}
