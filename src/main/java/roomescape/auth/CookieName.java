package roomescape.auth;

public enum CookieName {
    LOGIN_USER("loginUser");

    private final String value;

    CookieName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
