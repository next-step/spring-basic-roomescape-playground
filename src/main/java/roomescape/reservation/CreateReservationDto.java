package roomescape.reservation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateReservationDto {
    private final Long memberId;
    private final String name;
    private final String date;
    private final Long theme;
    private final Long time;
}
