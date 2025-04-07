package roomescape.waiting.service;

import roomescape.auth.domain.LoginMember;
import roomescape.member.domain.Member;
import roomescape.waiting.dto.WaitingRequest;
import roomescape.waiting.dto.WaitingResponse;

public interface WaitingService {
    WaitingResponse createWaiting(WaitingRequest request, Member member, LoginMember loginMember);

    void deleteWaiting(Long id);
}
