package roomescape.member;

public enum MemberRole {
    USER("USER"),
    ADMIN("ADMIN");

    private final String string;

    MemberRole(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
