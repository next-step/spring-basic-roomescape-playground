package roomescape.auth;

public class LoginTokens {
    private final String accessToken;
    private final String refreshToken;

    public LoginTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
