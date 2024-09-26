package roomescape.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import roomescape.member.domain.Member;

public class ReservationRequest {
    private String name;
    private String date;
    private Long theme;
    private Long time;
    @JsonIgnore
    private Member member;

    public ReservationRequest() {
    }

    public ReservationRequest(String name, String date, Long theme, Long time) {
        this.name = name;
        this.date = date;
        this.theme = theme;
        this.time = time;
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

    public Member getMember() {
        return member;
    }

    public void addName(String name) {
        this.name = name;
    }

    public void addMember(Member member) {
        this.member = member;
    }
}
