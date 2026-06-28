package roomescape.auth;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    public @Nullable AuthorizedMember tryAuthorizeRequest(HttpServletRequest request) {
        Object previousMember = request.getAttribute(AuthenticationInterceptor.ATTRIBUTE_AUTHORIZED_MEMBER_KEY);
        if(previousMember instanceof AuthorizedMember member) {
            return member;
        }

        return null;
    }
}
