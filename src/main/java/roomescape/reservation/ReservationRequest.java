package roomescape.reservation;

public class ReservationRequest {
    private String name;
    private String date;
    private Long themeId;
    private Long timeId;

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }

    public Long getTimeId() {
        return timeId;
    }
}
