package roomescape.member.enums;

public enum Role {

    ADMIN("관리자"),
    USER("사용자");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
