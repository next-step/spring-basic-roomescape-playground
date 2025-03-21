package roomescape;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class DataBaseCleaner {

    private List<String> tables = new ArrayList<>();

    @PersistenceContext
    private EntityManager entityManager;

    public DataBaseCleaner(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostConstruct
    void findTables() {
        this.tables = entityManager.getMetamodel()
                .getEntities().stream()
                .map(entityType -> entityType.getName().toUpperCase(Locale.ROOT))
                .toList();
    }

    @Transactional
    public void cleanup() {
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tables) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " RESTART IDENTITY").executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();

        entityManager.flush();
        entityManager.clear();
    }
}
