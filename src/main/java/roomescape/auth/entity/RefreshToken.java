package roomescape.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.member.entity.Member;

@Entity(name = "refresh_token")
@NoArgsConstructor
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Member member;

    @Column(nullable = false, length = 500)
    private String token;

    private RefreshToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    public static RefreshToken of(Member member, String token) {
        return new RefreshToken(member, token);
    }
}
