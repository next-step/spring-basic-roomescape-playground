package roomescape.waiting;

import jakarta.persistence.*;

@Entity
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;
    private String date;
    private Long timeId;
    private Long themeId;

    public Waiting() {
    }

    public Waiting(Long memberId, String date, Long timeId, Long themeId) {
        this.memberId = memberId;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public Long getId() {
        return id;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getMemberId() {
        return memberId;
    }

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
