package roomescape.auth;

public record TokenResponse(
        String accessToken
) {
    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
