package roomescape.time;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationTimeRepository extends CrudRepository<ParticipationTime, Long> {
    Optional<ParticipationTime> findById(Long id);

    List<ParticipationTime> findAll();


    Optional<ParticipationTime> findByTime(String time);


}
