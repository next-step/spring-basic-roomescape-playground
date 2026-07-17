package roomescape.time;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TimeRepository  {
    private final EntityManager entityManager;

    public TimeRepository(EntityManager entityManager){
        this.entityManager=entityManager;
    }

    public Time save(Time time){
        entityManager.persist(time);
        return time;
    }
    public List<Time> findAll(){
        return entityManager.createQuery(
                "SELECT t FROM Time t",
                Time.class
        ).getResultList();
    }

    public void deleteById(Long id){
        Time time =entityManager.find(Time.class,id);
        if(time !=null){
            entityManager.remove(time);
        }
    }

    public Optional<Time> findById(Long id){
        return Optional.ofNullable(
                entityManager.find(Time.class,id)
        );
    }
}
