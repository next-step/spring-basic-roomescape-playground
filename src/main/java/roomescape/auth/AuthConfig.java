package roomescape.auth;

public enum AuthConfig {

    TOKEN("token"),
    ROLE("role"),
    NAME("name"),
    ADMIN("admin");

    private String key;

    AuthConfig(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
