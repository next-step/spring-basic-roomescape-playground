package roomescape.member.service;

import static roomescape.auth.CookiesUtils.extractTokenFromCookie;
import static roomescape.auth.CookiesUtils.setTokenToCookie;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import roomescape.auth.JwtProvider;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
            new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse findMemberByName(String name) {
        Member member = memberRepository.getByName(name);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    @Transactional
    public void login(
        MemberRequest memberRequest,
        HttpServletResponse response
    ) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();
        Member member = memberRepository.findByEmailAndPassword(email, password);
        if (member == null)
            throw new IllegalArgumentException("Invalid email or password");

        String token = jwtProvider.createToken(member);
        setTokenToCookie(response, token);
    }

    @Transactional
    public MemberResponse loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        Long id = jwtProvider.getIdFromToken(token);
        Member member = memberRepository.getById(id);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
