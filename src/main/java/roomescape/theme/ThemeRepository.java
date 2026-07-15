package roomescape.theme;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeRepository  {
    private final EntityManager entityManager;

    public ThemeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Theme save(Theme theme){
        entityManager.persist(theme);
        return theme;
    }
    public List<Theme> findAll(){
        return entityManager.createQuery(
                "SELECT t FROM Theme t",
                Theme.class
        ).getResultList();
    }
    public void deleteById(Long id){
        Theme theme = entityManager.find(Theme.class,id);

        if(theme!=null){
            entityManager.remove(theme);
        }
    }
}
