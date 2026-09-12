package roomescape.domain.member.web.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.member.entity.Member;
import roomescape.domain.auth.service.AuthService;
import roomescape.domain.member.service.MemberService;
import roomescape.domain.member.web.dto.MemberRequest;
import roomescape.domain.member.web.dto.MemberResponse;
import roomescape.global.exception.ConflictException;

import java.net.URI;
import java.util.Map;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest memberRequest) {

        Member newMember;

        try {
            newMember = memberService.createMember(memberRequest.name(), memberRequest.email(), memberRequest.password());
        } catch (DuplicateKeyException e) {
            throw new ConflictException(null, Map.of("email", memberRequest.email()), "이미 가입된 이메일입니다.");
        }

        return ResponseEntity.created(URI.create("/members/" + newMember.getId())).body(MemberResponse.from(newMember));
    }
}
