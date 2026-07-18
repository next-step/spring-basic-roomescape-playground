package roomescape.auth;

import roomescape.member.MemberRole;

public record LoginMemberInfo(Long id, String name, String email, MemberRole role) {
}
