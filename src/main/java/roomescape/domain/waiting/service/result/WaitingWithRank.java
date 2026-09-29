package roomescape.domain.waiting.service.result;

import roomescape.domain.waiting.entity.ReserveWaiting;

public record WaitingWithRank(
        ReserveWaiting reserveWaiting,
        Long rank
) {

    public WaitingWithRank(ReserveWaiting reserveWaiting, Long rank) {
        validateFields(reserveWaiting, rank);
        this.reserveWaiting = reserveWaiting;
        this.rank = rank;
    }

    private void validateFields(ReserveWaiting reserveWaiting, Long rank) {
        if (reserveWaiting == null) {
            throw new IllegalArgumentException("reserveWaiting은 필수 필드입니다.");
        }

        if (reserveWaiting.getId() == null) {
            throw new IllegalArgumentException("reserveWaiting는 DB에 저장되어 ID를 갖는 객체여야 합니다.");
        }

        if (rank == null) {
            throw new IllegalArgumentException("rank는 필수 필드입니다.");
        }
    }
}
