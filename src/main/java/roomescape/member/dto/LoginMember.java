package roomescape.member.dto;

import roomescape.member.Member;

public class LoginMember extends Member {
    public LoginMember(Long id, String name, String email, String password) {
        super(id, name, email, password);
    }
}
