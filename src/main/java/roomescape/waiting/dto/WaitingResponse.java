package roomescape.waiting.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.waiting.Waiting;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WaitingResponse {

    private Long id;

    public static WaitingResponse from(Waiting waiting) {
        return new WaitingResponse(waiting.getId());
    }
}
