package roomescape.member;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "deleted", nullable = false)
    @ColumnDefault("false")
    private Boolean deleted;

    public Member(Long id, String name, String email, String role, Boolean deleted) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.deleted = deleted;
    }

    public Member(Long id, String name, String email, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Member(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}
