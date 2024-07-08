package roomescape.global.constant;

public enum AuthConstant {

    TOKEN("token"),
    ROLE("role"),
    NAME("name"),
    ADMIN("admin");

    private String value;

    AuthConstant(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
