package roomescape.member;

public enum Role {
    ADMIN("관리자"),
    USER("일반 사용자");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isNotAdmin() {
        return this != ADMIN;
    }
}
