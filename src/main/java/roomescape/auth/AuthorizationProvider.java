package roomescape.auth;

public interface AuthorizationProvider {

    MemberAuthorization createByPayload(String payload);

    String parseAuthorization(String token);
}
