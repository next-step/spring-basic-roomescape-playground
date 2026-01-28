package roomescape.reservation;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record MyReservationResponseDto(Long reservationId, String theme, String date, String time, String status) {

    public static MyReservationResponseDto from(Reservation reservation) {
        return new MyReservationResponseDto(
                reservation.getId(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue(),
                "예약"
        );
    }

}


