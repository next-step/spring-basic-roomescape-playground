package roomescape.waiting;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;

import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.member.Member;
import roomescape.member.Role;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.reservationTime.ReservationTimeRepository;
import roomescape.theme.ThemeRepository;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WaitingServiceTest {

    @Autowired
    private WaitingService waitingService;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private WaitingRepository waitingRepository;

    @Nested
    class create {

        @Test
        void 관리자가_만든_예약에_대기를_생성한다() {
            Member admin = new Member(1L, "어드민", "admin@email.com", "password", Role.ADMIN);
            Member brown = new Member(2L, "브라운", "brown@email.com", "password", Role.USER);
            Member manggo = new Member(3L, "망고", "manggo@email.com", "password", Role.USER);
            insertReservation(admin, "2025-04-03", 1L, 1L);

            WaitingRequest request = new WaitingRequest(LocalDate.parse("2025-04-03"), 1L, 1L);
            WaitingRankingResponse response = waitingService.create(brown, request);
            WaitingRankingResponse response2 = waitingService.create(manggo, request);

            assertThat(response.reservationId()).isGreaterThan(0L);
            assertThat(response.ranking()).isEqualTo(0L);
            assertThat(response2.ranking()).isEqualTo(1L);
        }

        @Test
        void 사용자가_만든_예약에_대기를_생성한다() {
            Member brown = new Member(2L, "브라운", "brown@email.com", "password", Role.USER);
            Member manggo = new Member(3L, "망고", "manggo@email.com", "password", Role.USER);
            insertReservation(brown, "2025-04-03", 2L, 2L);

            WaitingRequest request = new WaitingRequest(LocalDate.parse("2025-04-03"), 2L, 2L);
            WaitingRankingResponse response = waitingService.create(manggo, request);

            assertThat(response.reservationId()).isGreaterThan(0L);
            assertThat(response.ranking()).isEqualTo(0L);
        }

        @Test
        void 자신의예약에_대기를_만들면_예외를_던진다() {
            Member brown = new Member(2L, "브라운", "brown@email.com", "password", Role.USER);
            insertReservation(brown, "2025-04-03", 3L, 3L);

            WaitingRequest request = new WaitingRequest(LocalDate.parse("2025-04-03"), 3L, 3L);

            assertThatThrownBy(() -> waitingService.create(brown, request))
                    .isInstanceOf(RoomescapeBadRequestException.class)
                    .hasMessageContaining("본인이 예약한 방에는 대기를 할 수 없습니다.");
        }
    }

    @Nested
    class getMemberWaitings {

        @Test
        void 예약_대기_정보를_랭킹과_함께_얻는다() {
            Member admin = new Member(1L, "어드민", "admin@email.com", "password", Role.ADMIN);
            Member brown = new Member(2L, "브라운", "brown@email.com", "password", Role.USER);
            Member manggo = new Member(3L, "망고", "manggo@email.com", "password", Role.USER);
            Reservation reservation1 = insertReservation(admin, "2025-04-03", 2L, 2L);
            Reservation reservation2 = insertReservation(admin, "2025-04-04", 2L, 2L);

            insertWaiting(brown, reservation1);  // rank: 1
            insertWaiting(manggo, reservation1); // rank: 2

            insertWaiting(manggo, reservation2);
            insertWaiting(brown, reservation2);

            List<WaitingRankingResponse> brownWaitings = waitingService.getMemberWaitings(brown);
            List<WaitingRankingResponse> manggoWaitings = waitingService.getMemberWaitings(manggo);

            assertAll(
                    () -> assertThat(brownWaitings).hasSize(2),
                    () -> assertThat(brownWaitings.get(0).reservationId()).isEqualTo(reservation1.getId()),
                    () -> assertThat(brownWaitings.get(0).ranking()).isEqualTo(1L),
                    () -> assertThat(brownWaitings.get(1).reservationId()).isEqualTo(reservation2.getId()),
                    () -> assertThat(brownWaitings.get(1).ranking()).isEqualTo(2L)
            );

            assertAll(
                    () -> assertThat(manggoWaitings).hasSize(2),
                    () -> assertThat(manggoWaitings.get(0).reservationId()).isEqualTo(reservation1.getId()),
                    () -> assertThat(manggoWaitings.get(0).ranking()).isEqualTo(2L),
                    () -> assertThat(manggoWaitings.get(1).reservationId()).isEqualTo(reservation2.getId()),
                    () -> assertThat(manggoWaitings.get(1).ranking()).isEqualTo(1L)
            );
        }

        @Test
        void 대기_삭제_후_랭킹이_업데이트된다() {
            Member admin = new Member(1L, "어드민", "admin@email.com", "password", Role.ADMIN);
            Member brown = new Member(2L, "브라운", "brown@email.com", "password", Role.USER);
            Member manggo = new Member(3L, "망고", "manggo@email.com", "password", Role.USER);
            Reservation reservation = insertReservation(admin, "2025-04-03", 2L, 2L);

            Waiting waiting1 = insertWaiting(brown, reservation);   // rank: 1
            insertWaiting(manggo, reservation);                     // rank: 2

            List<WaitingRankingResponse> before = waitingService.getMemberWaitings(manggo);
            long beforeRank = before.get(0).ranking();

            waitingRepository.deleteById(waiting1.getId());
            List<WaitingRankingResponse> after = waitingService.getMemberWaitings(manggo);
            assertAll(
                    () -> assertThat(after.get(0).reservationId()).isEqualTo(reservation.getId()),
                    () -> assertThat(after.get(0).ranking()).isEqualTo(beforeRank - 1L)
            );
        }
    }

    private Reservation insertReservation(Member member, String date, long timeId, long themeId) {
        Reservation reservation = new Reservation(
                member,
                member.getName(),
                LocalDate.parse(date),
                reservationTimeRepository.findById(timeId).orElseThrow(),
                themeRepository.findById(themeId).orElseThrow()
        );
        return reservationRepository.save(reservation);
    }

    private Waiting insertWaiting(Member member, Reservation reservation) {
        return waitingRepository.save(new Waiting(member, reservation));
    }
}
