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
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(unique = true)
    private Member member;

    @Column(nullable = false, length = 500)
    private String token;

    public RefreshTokenEntity() {
    }

    public RefreshTokenEntity(Long id, Member member, String token) {
        this.id = id;
        this.member = member;
        this.token = token;
    }

    public RefreshTokenEntity(Member member, String token) {
        this(null, member, token);
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
