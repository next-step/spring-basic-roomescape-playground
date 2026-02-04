package roomescape.theme;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean deleted = false;

    public Theme() {
    }

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
