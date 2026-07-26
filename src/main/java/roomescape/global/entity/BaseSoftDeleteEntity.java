package roomescape.global.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.ZoneId;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseSoftDeleteEntity {

    private static final ZoneId KST_ZONE = ZoneId.of("Asia/Seoul");

    private LocalDateTime deletedAt;

    public void markDeleted() {
        this.deletedAt = LocalDateTime.now(KST_ZONE);
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
}
