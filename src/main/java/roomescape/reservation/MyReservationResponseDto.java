package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public class MyReservationResponseDto {
	private Long reservationId;
	private String theme;
	private String date;
	private String time;
	private String status;

	public MyReservationResponseDto(Long reservationId, String theme, String date, String time, String status) {
		this.reservationId = reservationId;
		this.theme = theme;
		this.date = date;
		this.time = time;
		this.status = status;
	}

	public static MyReservationResponseDto from(Reservation reservation) {
		return new MyReservationResponseDto(
				reservation.getId(),
				reservation.getTheme().getName(),
				reservation.getDate(),
				reservation.getTime().getValue(),
				"예약"
		);
	}

	@JsonIgnore
	public Long getId() {
		return reservationId;
	}
}


