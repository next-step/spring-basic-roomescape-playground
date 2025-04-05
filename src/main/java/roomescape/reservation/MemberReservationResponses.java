package roomescape.reservation;

import java.util.ArrayList;
import java.util.List;
import roomescape.waiting.WaitingRankingResponse;

public record MemberReservationResponses(List<MemberReservationResponse> responses) {

    public MemberReservationResponses addWaitings(List<WaitingRankingResponse> waitings) {
        List<MemberReservationResponse> newList = new ArrayList<>(responses);
        waitings.forEach(waiting ->
                newList.add(new MemberReservationResponse(waiting))
        );
        return new MemberReservationResponses(List.copyOf(newList));
    }
}
