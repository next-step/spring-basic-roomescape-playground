package roomescape.theme;

import jakarta.persistence.*;

//CREATE TABLE theme
//        (
//                id          BIGINT       NOT NULL AUTO_INCREMENT,
//                name        VARCHAR(255) NOT NULL,
//description VARCHAR(255) NOT NULL,
//deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
//PRIMARY KEY (id)
//);

@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    public Theme() {
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
}
