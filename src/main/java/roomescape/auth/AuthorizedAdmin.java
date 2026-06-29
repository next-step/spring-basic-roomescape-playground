package roomescape.auth;

import org.springframework.http.HttpStatus;
import roomescape.ApiException;
import roomescape.member.Member;


/**
 * {@code @RequestMapping} 등에서 AuthorizedAdmin를 argument로 받는 경우 AuthorizedMember에 추가적으로 권한도 확인합니다.
 * <p>
 * 참고로 AuthorizedMember를 받는 경우 role이 ADMIN이면 자동으로 AuthorizedAdmin이 오지 않습니다.
 */
public class AuthorizedAdmin extends AuthorizedMember {
    @SuppressWarnings("unused")
    public AuthorizedAdmin(String name, String email, Member.Role role) {
        super(name, email, role);

        if (role != Member.Role.ADMIN) {
            throw ApiException.status(HttpStatus.FORBIDDEN);
        }
    }
}
