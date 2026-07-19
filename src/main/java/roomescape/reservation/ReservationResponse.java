package roomescape.reservation;

import roomescape.member.Member;

public class ReservationResponse {
    private final Long id;
    private final Member member;
    private final String theme;
    private final String date;
    private final String time;
    private final String name;

    public ReservationResponse(Member member,String name,Long id, String theme, String date, String time) {
        this.member = member;
        this.theme = theme;
        this.date = date;
        this.time = time;
        this.name= name;
        this.id= id;
    }

    public String getName() {
        return name;
    }

    public Long getId(){
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
