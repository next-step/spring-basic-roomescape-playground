package roomescape.member;

public enum MemberRole {
    ADMIN,
    USER;

    public static MemberRole from(String role) {
        return MemberRole.valueOf(role);
    }
}
