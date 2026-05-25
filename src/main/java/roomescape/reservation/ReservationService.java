package roomescape.reservation;

import org.springframework.stereotype.Service;
import roomescape.member.LoginMember;
import roomescape.member.MemberDao;

import java.util.List;

@Service
public class ReservationService {
    private final MemberDao memberDao;
    private final ReservationDao reservationDao;

    public ReservationService(MemberDao memberDao, ReservationDao reservationDao) {
        this.memberDao = memberDao;
        this.reservationDao = reservationDao;
    }

    public ReservationResponse save(ReservationCreateCommand command, LoginMember loginMember) {
        String reservationName = findReservationName(command, loginMember);
        Reservation reservation = reservationDao.save(command, reservationName);

        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getTheme().getName(),
                reservation.getDate(),
                reservation.getTime().getValue()
        );
    }

    private String findReservationName(ReservationCreateCommand command, LoginMember loginMember) {
        if (command.name() != null && !command.name().isBlank()) {
            return command.name();
        }

        return memberDao.findById(loginMember.id()).getName();
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(
                        it.getId(),
                        it.getName(),
                        it.getTheme().getName(),
                        it.getDate(),
                        it.getTime().getValue()
                ))
                .toList();
    }
}