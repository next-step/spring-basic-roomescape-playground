package roomescape.login;

import roomescape.member.MemberResponse;

public interface LoginService {

    String login(String email, String password);
    MemberResponse validateToken(String token);
}
