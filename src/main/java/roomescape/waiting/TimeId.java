package roomescape.waiting;

import jakarta.persistence.Column;

public class TimeId {

    @Column(name = "time_id")
    private Long id;

    protected TimeId() {
    }

    public TimeId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
