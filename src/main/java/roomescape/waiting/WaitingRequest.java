package roomescape.waiting;

import com.fasterxml.jackson.annotation.JsonAlias;

public class WaitingRequest {

    private String date;

    @JsonAlias("time")
    private Long timeId;

    @JsonAlias("theme")
    private Long themeId;

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTimeId(Long timeId) {
        this.timeId = timeId;
    }

    public Long getTimeId() {
        return timeId;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
