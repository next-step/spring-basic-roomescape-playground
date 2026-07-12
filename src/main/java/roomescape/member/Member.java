package roomescape.member;

public record Member(Long id, String name, String email, String password, MemberRole role) {
    public Member(Long id, String name, String email, MemberRole role) {
        this(id, name, email, null, role);
    }

    public Member(String name, String email, String password, MemberRole role) {
        this(null, name, email, password, role);
    }
}
