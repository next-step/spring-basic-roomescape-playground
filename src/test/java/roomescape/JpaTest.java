package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationRepository;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.ReservationTimeRepository;

@DataJpaTest
public class JpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservationTimeRepository timeRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 사단계() {
        ReservationTime time = new ReservationTime("10:00");
        entityManager.persist(time);
        entityManager.flush();

        ReservationTime persistTime = timeRepository.findById(time.getId()).orElse(null);

        assertThat(persistTime.getTimeValue()).isEqualTo(time.getTimeValue());
    }

    @Test
    void 예약시간을_삭제해도_예약은_유지된다() {
        List<Reservation> reservations = reservationRepository.findAllWithReservationTime();
        reservations.stream()
                .forEach(reservation -> {
                    System.out.println("reservation = " + reservation);
                });

        timeRepository.deleteById(1L);

        List<Reservation> reservationsAfterDelete = reservationRepository.findAllWithReservationTime();
        reservationsAfterDelete.stream()
                .forEach(reservation -> {
                    System.out.println("reservation = " + reservation);
                });

        assertThat(reservationsAfterDelete.size()).isEqualTo(reservations.size());
    }

    @Test
    void queryTest() {
        Member member = memberRepository.findByName("브라운")
                .orElseThrow();
        reservationRepository.findAllForTest(member);
    }

//    @Test
//    @DisplayName("구현 코드 결과를 확인하는 목적이 아닌, 설정에 따른 쿼리를 확인해보기위한 테스트입니다.")
//    void ManyToOne_쿼리_확인() {
//
//        System.out.println("=== FindAll Join Query ===");
//
//        reservationRepository.findAll().stream()
//                .map(reservation -> reservation.getTime().getTimeValue())
//                .toList();
//
//        System.out.println("=== Fetch Join Query ===");
//
//        List<Reservation> reservationsByFetchJoin = reservationRepository.findAllWithForStudyByFetch();
//        reservationsByFetchJoin.stream()
//                .map(reservation -> reservation.getTime().getTimeValue())
//                .toList();
//
//        for (Reservation reservation : reservationsByFetchJoin) {
//            System.out.println(reservation.getName() + ", " + reservation.getId());
//            for (ForStudy study : reservation.getForStudies()) {
//                System.out.println("study = " + study.getContent());
//            }
//        }
//
//        System.out.println("=== Fetch Left Join Query ===");
//
//        List<Reservation> reservationsByFetchLeftJoin = reservationRepository.findAllWithForStudyByLeftFetch();
//        reservationsByFetchLeftJoin.stream()
//                .map(reservation -> reservation.getTime().getTimeValue())
//                .toList();
//
//        for (Reservation reservation : reservationsByFetchLeftJoin) {
//            System.out.println(reservation.getName() + ", " + reservation.getId());
//            for (ForStudy study : reservation.getForStudies()) {
//                System.out.println("study = " + study.getContent());
//            }
//        }
//
//        System.out.println("=== Entity Graph Query ===");
//
//        List<Reservation> reservationsByEntityGraph = reservationRepository.findAllWithForStudyByEntityGraph();
//        reservationsByEntityGraph.stream()
//                .map(reservation -> reservation.getTime().getTimeValue())
//                .toList();
//
//        for (Reservation reservation : reservationsByEntityGraph) {
//            System.out.println(reservation.getName() + ", " + reservation.getId());
//            for (ForStudy study : reservation.getForStudies()) {
//                System.out.println("study = " + study.getContent());
//            }
//        }
//
//        assertThat(reservationsByFetchJoin.size()).isEqualTo(2);
//        assertThat(reservationsByFetchLeftJoin.size()).isEqualTo(4);
//        assertThat(reservationsByEntityGraph.size()).isEqualTo(4);
//        // hibernate 6 위 버전부터는 OneToMany와 fetch join으로 인한 중복을 hibernate에서 처리해준다... + entityGraph
//    }
}
