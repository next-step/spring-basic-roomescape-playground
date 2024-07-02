package roomescape.waiting.dto;

import roomescape.waiting.Waiting;

public record WaitingWithRank(Waiting waiting,
       Long rank) {
}