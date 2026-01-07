package roomescape.waiting;

import lombok.Getter;

@Getter
public class WaitingResponseDto {
	private Long id;

	public WaitingResponseDto() {
	}

	public WaitingResponseDto(Long id) {
		this.id = id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}


