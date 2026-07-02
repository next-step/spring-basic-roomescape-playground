package roomescape.auth;

import java.util.Objects;
import roomescape.member.Member;

/**
 * `@RequestMapping` 등에서 `AuthorizedMember`를 argument로 받는 경우 클라이언트가 해당 경로를 사용하려면 유효한 세션 토큰을 가지고 있어야 합니다. 이 때, 해당
 * `AuthorizedMember` 값은 jwt 세션 토큰의 claims payload에서 가져옵니다.
 */
public class AuthorizedMember {
    private final long id;
    private final String name;
    private final String email;
    private final Member.Role role;

    public AuthorizedMember(long id, String name, String email, Member.Role role) {
        Objects.requireNonNull(name, "name이 null일 수 없습니다.");
        Objects.requireNonNull(email, "email이 null일 수 없습니다.");
        Objects.requireNonNull(role, "role이 null일 수 없습니다.");

        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public static AuthorizedMember from(Member member) {
        return new AuthorizedMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    public Member.Role role() {
        return role;
    }
}
