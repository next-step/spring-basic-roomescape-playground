package roomescape.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;

public class ReservationResponse {

    private Long id;

    private String name;

    @JsonProperty("themeId")
    private String themeId;

    private LocalDate date;

    @JsonProperty("timeId")
    private String timeId;

    protected ReservationResponse(Long id, String name, String themeId, LocalDate date, String timeId) {
        this.id = id;
        this.name = name;
        this.themeId = themeId;
        this.date = date;
        this.timeId = timeId;
    }

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName();
        this.themeId = reservation.getTheme().getName();
        this.date = reservation.getDate();
        this.timeId = reservation.getTime().getValue();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getThemeId() {
        return themeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getTimeId() {
        return timeId;
    }
}
