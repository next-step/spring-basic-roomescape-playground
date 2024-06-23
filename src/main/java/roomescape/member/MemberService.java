package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.api.JwtDecoder;
import roomescape.api.JwtProvider;
import roomescape.exception.NotFoundException;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse.Create createMember(MemberRequest.Create memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse.Create(member.getId(), member.getName(), member.getEmail());
    }

    public String loginMember(MemberRequest.Login request) {
        Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if(member == null){
            throw new NotFoundException("유저를 찾을 수 없습니다.");
        }
        return jwtProvider.createToken(member);
    }

    public MemberResponse.Check checkMember(String token) {
        Long memberId = JwtDecoder.decodeJwtToken(token);
        Member member =  memberDao.findById(memberId);
        if(member == null) {
            throw new NotFoundException("유저를 찾을 수 없습니다.");
        }
        return MemberResponse.Check.from(member);
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public Member findByEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);
        if(member == null ){
            throw new NotFoundException("유저를 찾을 수 없습니다.");
        }
        return member;
    }
}
