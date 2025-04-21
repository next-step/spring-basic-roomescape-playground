package roomescape.theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @ColumnDefault("false")
    @Column(name = "deleted", nullable = false)
    private boolean isDeleted;

    protected Theme() {
    }

    private Theme(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isDeleted = false;
    }

    public Theme(String name, String description) {
        this(null, name, description);
    }

    public void markAsDeleted() {
        this.isDeleted = true;
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
}
