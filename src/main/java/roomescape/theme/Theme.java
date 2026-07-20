package roomescape.theme;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "theme")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @JsonIgnore
    private boolean deleted;

    public Theme() {
    }

    public Theme(String name, String description) {
        this.name = name;
        this.description = description;
        this.deleted = false;
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public void delete() {
        this.deleted = true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
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

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }
}
