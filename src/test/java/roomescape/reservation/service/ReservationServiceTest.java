package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.member.auth.AuthorizationException;
import roomescape.member.domain.LoginMember;
import roomescape.member.service.MemberService;
import roomescape.reservation.repository.ReservationDao;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.repository.TimeDao;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private MemberService memberService;

    @Mock
    private ThemeDao themeDao;

    @Mock
    private TimeDao timeDao;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void non_admin_cannot_delete_reservation() {
        LoginMember member = new LoginMember(1L, "회원", "member@email.com", "USER");

        assertThatThrownBy(() -> reservationService.deleteById(1L, member))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("예약을 삭제할 권한이 없습니다.");
        verifyNoInteractions(reservationDao);
    }

    @Test
    void admin_can_delete_reservation() {
        LoginMember admin = new LoginMember(1L, "관리자", "admin@email.com", "ADMIN");

        reservationService.deleteById(1L, admin);

        verify(reservationDao).deleteById(1L);
    }
}
