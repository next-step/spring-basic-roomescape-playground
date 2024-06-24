package roomescape.auth;

public enum AuthConfig {

    TOKEN("token"),
    ROLE("role"),
    NAME("name"),
    ADMIN("admin");

    private String value;

    AuthConfig(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
