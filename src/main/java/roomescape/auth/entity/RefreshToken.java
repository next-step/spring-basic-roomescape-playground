package roomescape.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import roomescape.member.entity.Member;

@Entity(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(unique = true)
    private Member member;

    @Column(nullable = false, length = 500)
    private String token;

    public RefreshToken() {
    }

    public RefreshToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public String getToken() {
        return token;
    }
}
