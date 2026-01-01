package roomescape.dto;

public record ReservationRequest(
        String name,
        Long memberId,
        String date,
        Long theme,
        Long time
) { }
