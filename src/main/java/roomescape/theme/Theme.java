package roomescape.theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theme_id", nullable = false)
    private Long id;
    private String name;
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


    // 2. 객체를 생성하고 반환하는 static 메서드를 제공합니다.
    public static Theme of(String name, String description) {
        // 내부 로직을 수행할 수 있습니다.
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
