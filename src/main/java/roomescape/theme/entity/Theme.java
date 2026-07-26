package roomescape.theme.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import roomescape.global.entity.BaseSoftDeleteEntity;

@Entity(name = "theme")
public class Theme extends BaseSoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Theme() {
    }

    private Theme(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static Theme of(String name, String description) {
        return new Theme(name, description);
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
