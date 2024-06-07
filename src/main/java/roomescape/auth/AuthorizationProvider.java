package roomescape.auth;

public interface AuthorizationProvider {

    MemberCredential create(MemberAuthContext member);

    MemberAuthContext parseCredential(MemberCredential token);
}
