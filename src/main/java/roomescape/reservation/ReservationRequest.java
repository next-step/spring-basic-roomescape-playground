package roomescape.reservation;

public record ReservationRequest(
        String name,
        String date,
        Long theme,
        Long time
) {
    public ReservationRequest {
        validateDate(date);
        validateTheme(theme);
        validateTime(time);
    }
    
    private void validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid date");
        }
    }

    private void validateTheme(Long theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Invalid theme");
        }
    }

    private void validateTime(Long time) {
        if (time == null) {
            throw new IllegalArgumentException("Invalid time");
        }
    }
}
