package roomescape.theme;

import jakarta.persistence.*;
import roomescape.reservation.Reservation;

import java.util.List;

@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    public Theme() {
    }

    @OneToMany(mappedBy = "theme")
    private List<Reservation> reservations;

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
