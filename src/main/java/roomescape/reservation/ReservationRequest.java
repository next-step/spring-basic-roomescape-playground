package roomescape.reservation;

public record ReservationRequest(
        String name,
        String date,
        String theme,
        String time
) {
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTheme() {
        return theme;
    }

    public String getTime() {
        return time;
    }
}
