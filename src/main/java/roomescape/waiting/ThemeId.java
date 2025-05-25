package roomescape.waiting;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ThemeId {

    @Column(name = "theme_id")
    private Long id;

    protected ThemeId() {
    }

    public ThemeId(Long id) {
        this.id = id;
    }
}
