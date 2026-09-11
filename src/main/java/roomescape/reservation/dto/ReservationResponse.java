package roomescape.reservation.dto;

public record ReservationResponse(Long id, String name, String theme, String date, String time) {

    public String getName() {
        return name;
    }
}
