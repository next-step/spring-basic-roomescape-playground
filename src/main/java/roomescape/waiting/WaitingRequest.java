package roomescape.waiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WaitingRequest {
    private String date;
    private Long theme;
    private Long time;
}
