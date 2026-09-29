package roomescape.domain.member.web.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.domain.auth.web.support.annotation.Public;
import roomescape.domain.member.entity.Member;
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

    @Public
    @PostMapping("/members")
    public ResponseEntity<MemberResponse> createMember(@Valid @RequestBody MemberRequest memberRequest) {

        Member newMember;

        try {
            newMember = memberService.createMember(memberRequest.name(), memberRequest.email(), memberRequest.password());
        } catch (DataIntegrityViolationException e) {
            Map<String, String> errorData = memberService.determineDuplicate(memberRequest.email(), memberRequest.name());
            throw new ConflictException(null, Map.of(errorData.get("key"), errorData.get("value")), errorData.get("cause"));
        }

        return ResponseEntity.created(URI.create("/members/" + newMember.getId())).body(MemberResponse.from(newMember));
    }
}
