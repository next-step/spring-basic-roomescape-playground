package roomescape.member;

public interface TokenProvider {

    String createToken(Member member);

    Long parse(String token);
}
