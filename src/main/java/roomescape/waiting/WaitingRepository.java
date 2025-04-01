package roomescape.waiting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {


}
