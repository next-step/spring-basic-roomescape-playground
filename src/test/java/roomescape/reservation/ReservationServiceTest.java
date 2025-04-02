package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Nested
    class create {

        @Test
        void 이름으로_예약을_생성한다() {
            ReservationRequest request = new ReservationRequest(
                    "사용자1", LocalDate.parse("2025-04-10"), 1L, 1L);

            ReservationResponse response = reservationService.create(request);
            Reservation saved = reservationRepository.findByDateAndReservationTime_IdAndTheme_Id(
                    LocalDate.parse("2025-04-10"), 1L, 1L).orElseThrow();

            assertThat(response.name()).isEqualTo(saved.getName());
        }

        @Test
        void 멤버로_예약을_생성한다() {
            Member member = memberRepository.findByEmail("brown@email.com").orElseThrow();
            ReservationRequest request = new ReservationRequest(member.getName()
                    , LocalDate.parse("2025-04-11"), 2L, 2L);

            ReservationResponse response = reservationService.saveWithMember(request, member);

            Reservation saved = reservationRepository.findByDateAndReservationTime_IdAndTheme_Id(
                    LocalDate.parse("2025-04-11"), 2L, 2L
            ).orElseThrow();

            assertThat(saved.getMember().getId()).isEqualTo(member.getId());
        }

        @Test
        void 중복_예약을하면_예외를_던진다() {
            Reservation reservation = reservationRepository.findById(1L).orElseThrow();
            ReservationRequest request = new ReservationRequest(
                    "중복맨", reservation.getDate(),
                    reservation.getTheme().getId(),
                    reservation.getTime().getId()
            );

            assertThatThrownBy(() -> reservationService.create(request))
                    .isInstanceOf(RoomescapeBadRequestException.class)
                    .hasMessageContaining("이미 예약 된 방입니다.");
        }
    }
}

