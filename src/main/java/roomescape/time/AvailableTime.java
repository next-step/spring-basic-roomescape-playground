package roomescape.time;

public record AvailableTime(
        Long timeId,
        String time,
        boolean booked
) {
}
