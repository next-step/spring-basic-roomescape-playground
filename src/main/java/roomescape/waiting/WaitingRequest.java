package roomescape.waiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class WaitingRequest {
    private String date;
    private Long theme;
    private Long time;


}