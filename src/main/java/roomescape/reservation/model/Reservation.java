package roomescape.reservation.model;

import jakarta.persistence.*;
import roomescape.member.model.Member;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation(Member member, Inventory inventory, ReservationStatus status) {
        this.member = member;
        this.inventory = inventory;
        this.status = status;
    }

    public Reservation() {

    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
