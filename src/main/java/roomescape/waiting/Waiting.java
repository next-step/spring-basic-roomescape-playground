package roomescape.waiting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import roomescape.member.model.Member;
import roomescape.theme.Theme;
import roomescape.time.Time;

@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String date;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    @ManyToOne
    private Member member;

    public Waiting(String date, Time time, Theme theme, Member member) {
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.member = member;
    }

    public Waiting() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}