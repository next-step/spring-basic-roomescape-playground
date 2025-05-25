package roomescape.waiting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MemberId {

    @Column(name = "member_id")
    private Long id;

    protected MemberId() {
    }

    public MemberId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
