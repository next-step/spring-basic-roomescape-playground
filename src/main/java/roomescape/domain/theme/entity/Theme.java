package roomescape.domain.theme.entity;

import jakarta.persistence.*;

@Entity
public class Theme {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(nullable = false, length = 255)
    private String description;

    protected Theme() {
    }

    public Theme(String name, String description) {
        validateFields(name, description);
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

    private void validateFields(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Theme를 생성하기 위해 name은 필수 필드입니다.");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Theme를 생성하기 위해 description은 필수 필드입니다.");
        }
    }
}
