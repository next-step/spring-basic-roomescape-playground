package roomescape.waiting;

public record WaitingRequest(
        String name,
        String date,
        Long timeId,
        Long themeId
) {
    public WaitingRequest {
        validateDate(date);
        validateTime(timeId);
        validateTheme(themeId);
    }

    private void validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("Invalid date");
        }
    }

    private void validateTime(Long time) {
        if (time == null) {
            throw new IllegalArgumentException("Invalid time");
        }
    }

    private void validateTheme(Long theme) {
        if (theme == null) {
            throw new IllegalArgumentException("Invalid theme");
        }
    }
}
