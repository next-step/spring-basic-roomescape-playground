package roomescape.waiting.domain;

import java.util.List;

public class Waitings {

    private static final int BASE_RANK = 1;

    private final List<Waiting> waitings;

    public Waitings(final List<Waiting> waitings) {
        this.waitings = waitings;
    }

    public long calculateRank(Waiting target) {
        return waitings.stream()
                .filter(waiting -> waiting.isBefore(target))
                .count() + BASE_RANK;
    }
}
