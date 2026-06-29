package roomescape.auth.dto;

public record AuthResult(
        String token,
        MemberInfo memberInfo
 ) {}