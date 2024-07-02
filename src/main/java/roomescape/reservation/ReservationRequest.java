package roomescape.reservation;

public class ReservationRequest {
    private String name;
    private String date;
    private Long theme;
    private Long time;

    public void setMember(Long member) {
        this.member = member;
    }

    private Long member;

    public Long getMember() {
        return member;
    }

    public String getName() {
        return name;
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
    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTheme(Long theme) {
        this.theme = theme;
    }

    public void setTime(Long time) {
        this.time = time;
    }


}