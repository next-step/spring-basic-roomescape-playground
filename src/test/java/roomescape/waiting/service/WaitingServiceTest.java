package roomescape.waiting.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.fixture.FixtureConfig;
import roomescape.fixture.FixtureGenerator;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.waiting.dto.request.WaitingRequest;
import roomescape.waiting.dto.response.WaitingResponse;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(FixtureConfig.class)
class WaitingServiceTest {

    @Autowired
    private FixtureGenerator fixtureGenerator;

    @Autowired
    private WaitingService waitingService;

    @Autowired
    private ReservationRepository reservationRepository;

    private Theme theme;
    private Time time;

    @BeforeEach
    void setUp() {
        theme = fixtureGenerator.createTheme();
        time = fixtureGenerator.createTime();
    }

    @Test
    void 예약_대기를_신청할_수_있다() {
        // given
        LoginMember loginMember1 = fixtureGenerator.createLoginMember("멤버1", "member1@email.com");
        LoginMember loginMember2 = fixtureGenerator.createLoginMember("멤버2", "member2@email.com");

        LocalDate date = LocalDate.of(2025, 3, 30);

        Reservation reservation = new Reservation(loginMember1.id(), loginMember1.name(), date, time, theme);
        reservationRepository.save(reservation);

        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());
        int expectedWaitingNumber = 1;
        // when
        WaitingResponse response = waitingService.createWaiting(request, loginMember2);
        // then
        assertThat(response.waitingNumber()).isEqualTo(expectedWaitingNumber);
    }

    @Test
    void 예약이_존재하지_않는_상태에서_대기를_신청하면_예외가_발생한다() {
        // given
        LoginMember loginMember = fixtureGenerator.createLoginMember("멤버", "member@email.com");

        LocalDate date = LocalDate.of(2025, 3, 30);
        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());

        // when & then
        assertThatThrownBy(() -> waitingService.createWaiting(request, loginMember))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.RESERVATION_NOT_FOUND.getMessage());
    }

    @Test
    void 이미_예약을_한_상태에서_예약_대기를_신청하면_예외가_발생한다() {
        // given
        LoginMember loginMember = fixtureGenerator.createLoginMember("멤버", "member@email.com");

        LocalDate date = LocalDate.of(2025, 3, 30);

        Reservation reservation = new Reservation(loginMember.id(), loginMember.name(), date, time, theme);
        reservationRepository.save(reservation);

        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());
        // when & then
        assertThatThrownBy(() -> waitingService.createWaiting(request, loginMember))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.RESERVATION_ALREADY_EXISTS.getMessage());
    }

    @Test
    void 예약_대기를_중복으로_신청하면_예외가_발생한다() {
        // given
        LoginMember loginMember1 = fixtureGenerator.createLoginMember("멤버1", "member1@email.com");
        LoginMember loginMember2 = fixtureGenerator.createLoginMember("멤버2", "member2@email.com");

        LocalDate date = LocalDate.of(2025, 3, 30);

        Reservation reservation = new Reservation(loginMember1.id(), loginMember1.name(), date, time, theme);
        reservationRepository.save(reservation);

        WaitingRequest request = new WaitingRequest(date, time.getId(), theme.getId());
        waitingService.createWaiting(request, loginMember2);

        // when & then
        assertThatThrownBy(() -> waitingService.createWaiting(request, loginMember2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.WAITING_ALREADY_EXISTS.getMessage());
    }
}
