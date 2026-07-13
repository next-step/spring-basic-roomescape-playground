package roomescape.reservation.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import roomescape.reservation.model.Date;
import roomescape.reservation.model.Inventory;
import roomescape.theme.model.Theme;
import roomescape.time.model.Time;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    List<Inventory> findByDateAndTheme(Date date, Theme theme);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    Optional<Inventory> findByDateAndTimeAndTheme(Date date, Time time, Theme theme);
}
