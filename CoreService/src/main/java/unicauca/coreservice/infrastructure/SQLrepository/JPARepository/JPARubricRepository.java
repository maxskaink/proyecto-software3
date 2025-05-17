package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;

import java.util.List;
import java.util.Optional;

public interface JPARubricRepository extends JpaRepository<RubricEntity, Integer> {
    List<RubricEntity> findByIsActivatedTrue();
    Optional<RubricEntity> findActiveRubricByRubricId(Integer rubricId);
}
