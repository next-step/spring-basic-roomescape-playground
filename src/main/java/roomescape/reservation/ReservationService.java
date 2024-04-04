package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

@Service
public class ReservationService {
    private ReservationDao reservationDao;
    private CookieProvider cookieProvider;
    private TokenProvider tokenProvider;

    public ReservationService(ReservationDao reservationDao, CookieProvider cookieProvider, TokenProvider tokenProvider) {
        this.reservationDao = reservationDao;
        this.cookieProvider = cookieProvider;
        this.tokenProvider = tokenProvider;
    }

    public ReservationResponse save(ReservationRequest reservationRequest, HttpServletRequest httpServletRequest) {
        String token = cookieProvider.extractTokenFromCookie(httpServletRequest.getCookies());
        String name = tokenProvider.getMemberNameFromToken(token);
        if(reservationRequest.getName() == null) reservationRequest.setName(name);
        Reservation reservation = reservationDao.save(reservationRequest);

        return new ReservationResponse(reservation.getId(), reservationRequest.getName(), reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it.getId(), it.getName(), it.getTheme().getName(), it.getDate(), it.getTime().getValue()))
                .toList();
    }
}
