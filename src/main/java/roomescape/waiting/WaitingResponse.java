package roomescape.waiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WaitingResponse {
    private final Long id;
    private final String theme;
    private final String date;
    private final String time;
    private final int waitingNumber;
}
