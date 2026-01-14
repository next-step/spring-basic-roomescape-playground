package roomescape.theme;

import jakarta.persistence.*;

@Entity
@Table(name = "theme")
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean deleted = false;

    protected Theme() {
    }

    public Theme(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void markDeleted() {
        this.deleted = true;
    }
}
