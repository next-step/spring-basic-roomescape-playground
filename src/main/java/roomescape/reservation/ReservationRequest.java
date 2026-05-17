package roomescape.reservation;

// record로 구현 불가능 -> setName으로 인해 name이 final이 아니라서
public class ReservationRequest {
    private String name;
    private String date;
    private Long theme;
    private Long time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public Long getTheme() {
        return theme;
    }

    public Long getTime() {
        return time;
    }
}
