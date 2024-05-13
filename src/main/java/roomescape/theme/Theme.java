package roomescape.theme;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;

    public Theme() {
    }

    public Theme(Long id, String name, String description, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deleted = deleted;
    }

    public Theme(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getDeleted() {
        return deleted;
    }
}
