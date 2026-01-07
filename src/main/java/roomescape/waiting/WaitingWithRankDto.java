package roomescape.waiting;

public class WaitingWithRankDto {
	private Waiting waiting;
	private Long rank;

	public WaitingWithRankDto(Waiting waiting, Long rank) {
		this.waiting = waiting;
		this.rank = rank;
	}

	public Waiting getWaiting() {
		return waiting;
	}

	public Long getRank() {
		return rank;
	}
}


