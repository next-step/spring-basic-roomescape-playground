package roomescape.auth;

import roomescape.member.Member;

/**
 * `@RequestMapping` 등에서 `AuthorizedMember`를 argument로 받는 경우 클라이언트가 해당 경로를 사용하려면 유효한 세션 토큰을 가지고 있어야 합니다. 이 때, 해당
 * `AuthorizedMember` 값은 jwt 세션 토큰의 claims payload에서 가져옵니다.
 */
public record AuthorizedMember(
        String name,
        String email,
        Member.Role role
) {
    public static AuthorizedMember from(Member member) {
        return new AuthorizedMember(member.getName(), member.getEmail(), member.getRole());
    }
}
