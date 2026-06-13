package roomescape.reservation;

public record ReservationRequest(String name, String date, Long theme, Long time) {
    public boolean isValid() {
        return date != null && theme != null && time != null;
    }
}