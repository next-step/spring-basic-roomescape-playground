package roomescape.reservation.service;

import java.util.List;
import roomescape.auth.domain.LoginMember;
import roomescape.reservation.dto.AdminReservationRequest;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.ReservationResponse;

public interface ReservationService {
    ReservationResponse saveUserReservation(ReservationRequest request, LoginMember loginMember);

    ReservationResponse saveAdminReservation(AdminReservationRequest request, LoginMember loginMember);

    List<ReservationResponse> findReservations(LoginMember loginMember);

    void deleteReservation(Long id, LoginMember loginMember);
}
