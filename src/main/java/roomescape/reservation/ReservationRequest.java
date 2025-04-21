package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("date")
    private String date;

    @JsonProperty("theme")
    private Long themeId;

    @JsonProperty("time")
    private Long timeId;

    public ReservationRequest() {
    }

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
