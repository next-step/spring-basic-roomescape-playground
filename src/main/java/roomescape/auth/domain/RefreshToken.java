package roomescape.auth.domain;

public class RefreshToken {
    private Long id;
    private Long memberId;
    private String token;

    public RefreshToken(Long id, Long memberId, String token) {
        this.id = id;
        this.memberId = memberId;
        this.token = token;
    }

    public RefreshToken(Long memberId, String token) {
        this(null, memberId, token);
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getToken() {
        return token;
    }
}
