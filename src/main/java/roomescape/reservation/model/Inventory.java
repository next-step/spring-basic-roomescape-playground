package roomescape.reservation.model;

import jakarta.persistence.*;
import roomescape.theme.model.Theme;
import roomescape.time.model.Time;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "date_value"))
    private Date date;

    @ManyToOne
    private Time time;

    @ManyToOne
    private Theme theme;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    public Inventory(Date date, Time time, Theme theme) {
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.reservations = new ArrayList<>();
    }

    public Inventory() {
        this.reservations = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservations);
    }
}
