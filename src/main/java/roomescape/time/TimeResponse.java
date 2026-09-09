package roomescape.time;

public record TimeResponse(
        Long id,
        String value
) {
    public static TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getValue());
    }
}
