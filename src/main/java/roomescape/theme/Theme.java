package roomescape.theme;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Theme {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
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
