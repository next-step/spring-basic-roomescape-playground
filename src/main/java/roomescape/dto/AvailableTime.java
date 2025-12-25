package roomescape.dto;

public record AvailableTime(
        Long timeId,
        String time,
        boolean booked
) { }
