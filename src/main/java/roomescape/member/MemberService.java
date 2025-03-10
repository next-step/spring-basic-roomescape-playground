package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {

    public static final String ROLE_VALUE = "USER";

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = registerMember(memberRequest);
        return toMemberResponse(member);
    }

    private MemberResponse toMemberResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    private Member registerMember(MemberRequest memberRequest) {
        return memberDao.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), ROLE_VALUE));
    }

    public String authenticateAndGetToken(LoginRequest loginRequest) {
        MemberResponse memberResponse = findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return jwtProvider.createToken(memberResponse);
    }

    private MemberResponse findByEmailAndPassword(String email, String password) {
        Member member = getMemberByEmailAndPassword(email, password);
        return toMemberResponse(member);
    }

    private Member getMemberByEmailAndPassword(String email, String password) {
        return memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

    public AuthUserNameResponse findByToken(String token) {
        Long memberId = jwtProvider.parseMemberIdFrom(token);

        Member member = getMemberById(memberId);

        return new AuthUserNameResponse(member.getName());
    }

    private Member getMemberById(Long memberId) {
        return memberDao.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Member not found"));
    }

}
