package roomescape.auth;

public interface AuthorizationProvider {

    MemberAuthorization createByPayload(String payload);
}
