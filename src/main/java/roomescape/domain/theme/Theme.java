package roomescape.domain.theme;

public class Theme {
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
