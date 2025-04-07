package roomescape.reservation.service;

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
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.MyReservationResponse;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.waiting.domain.Status;
import roomescape.waiting.domain.Waiting;
import roomescape.waiting.repository.WaitingRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Import(FixtureConfig.class)
class ReservationServiceTest {

    @Autowired
    private FixtureGenerator fixtureGenerator;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WaitingRepository waitingRepository;

    private Theme theme;
    private Time time;

    @BeforeEach
    void setUp() {
        theme = fixtureGenerator.createTheme();
        time = fixtureGenerator.createTime();
    }

    @Test
    void 관리자가_직접_고객의_예약을_생성할_수_있다() {
        // given
        LoginMember loginMember = fixtureGenerator.createLoginMember("멤버", "member@email.com");

        ReservationRequest request = new ReservationRequest(loginMember.name(), LocalDate.of(2025, 3, 15), theme.getId(), time.getId());
        // when
        ReservationResponse response = reservationService.save(request, null);
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(loginMember.name()),
                () -> assertThat(response.getTheme()).isEqualTo(theme.getName()),
                () -> assertThat(response.getDate()).isEqualTo(request.getDate()),
                () -> assertThat(response.getTime()).isEqualTo(time.getValue())
        );
    }

    @Test
    void 로그인_정보를_활용하여_예약을_생성할_수_있다() {
        // given
        LoginMember loginMember = fixtureGenerator.createLoginMember("멤버", "member@email.com");

        ReservationRequest request = new ReservationRequest(null, LocalDate.of(2025, 3, 15), theme.getId(), time.getId());
        // when
        ReservationResponse response = reservationService.save(request, loginMember);
        // then
        assertAll(
                () -> assertThat(response.getName()).isEqualTo(loginMember.name()),
                () -> assertThat(response.getTheme()).isEqualTo(theme.getName()),
                () -> assertThat(response.getDate()).isEqualTo(request.getDate()),
                () -> assertThat(response.getTime()).isEqualTo(time.getValue())
        );
    }

    @Test
    void 동일한_날짜_시간_및_테마를_가진_예약이_존재하면_예외가_발생한다() {
        // given
        LoginMember loginMember1 = fixtureGenerator.createLoginMember("멤버1", "member1@email.com");
        LoginMember loginMember2 = fixtureGenerator.createLoginMember("멤버2", "member2@email.com");

        ReservationRequest request1 = new ReservationRequest(null, LocalDate.of(2025, 3, 15), theme.getId(), time.getId());
        reservationService.save(request1, loginMember1);

        ReservationRequest request2 = new ReservationRequest(null, LocalDate.of(2025, 3, 15), theme.getId(), time.getId());
        // when & then
        assertThatThrownBy(() -> reservationService.save(request2, loginMember2))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionMessage.RESERVATION_ALREADY_EXISTS.getMessage());
    }

    @Test
    void 예약_및_대기_목록을_조회한다() {
        // given
        LoginMember loginMember = fixtureGenerator.createLoginMember("멤버", "member@email.com");

        LocalDate date1 = LocalDate.of(2025, 3, 30);
        Reservation reservation = new Reservation(loginMember.id(), loginMember.name(), date1, time, theme);
        reservationRepository.save(reservation);

        LocalDate date2 = LocalDate.of(2025, 3, 31);
        Waiting waiting = new Waiting(loginMember.id(), loginMember.name(), date2, time, theme);
        waitingRepository.save(waiting);
        // when
        List<MyReservationResponse> responses = reservationService.findMyReservations(loginMember);
        // then
        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses).anyMatch(response -> response.id() == (reservation.getId())),
                () -> assertThat(responses).anyMatch(response -> response.id() == (waiting.getId()))
        );
    }

    @Test
    void 예약을_취소할_수_있다() {
        // given
        LoginMember loginMember = fixtureGenerator.createLoginMember("멤버", "member@email.com");

        LocalDate date = LocalDate.of(2025, 3, 30);
        Reservation reservation = new Reservation(loginMember.id(), loginMember.name(), date, time, theme);
        reservationRepository.save(reservation);

        // when
        reservationService.delete(reservation.getId());
        // then
        List<MyReservationResponse> responses = reservationService.findMyReservations(loginMember);
        assertThat(responses).isEmpty();
    }

    @Test
    void 예약을_취소하면_첫번째_대기멤버가_예약에_성공한다() {
        // given
        LoginMember loginMember1 = fixtureGenerator.createLoginMember("멤버1", "member1@email.com");
        LoginMember loginMember2 = fixtureGenerator.createLoginMember("멤버2", "member2@email.com");

        LocalDate date = LocalDate.of(2025, 3, 30);

        Reservation reservation = new Reservation(loginMember1.id(), loginMember1.name(), date, time, theme);
        reservationRepository.save(reservation);

        Waiting waiting = new Waiting(loginMember2.id(), loginMember2.name(), date, time, theme);
        waitingRepository.save(waiting);
        // when
        reservationService.delete(reservation.getId());
        // then
        List<MyReservationResponse> responses = reservationService.findMyReservations(loginMember2);
        assertAll(
                () -> assertThat(responses).hasSize(1),
                () -> assertThat(responses).anyMatch(response -> Status.CONFIRMED.getDescription().equals(response.status()))
        );
    }
}
