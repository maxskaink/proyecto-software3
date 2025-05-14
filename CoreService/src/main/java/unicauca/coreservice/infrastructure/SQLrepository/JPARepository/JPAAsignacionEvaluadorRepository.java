package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignEvaluatorEntity;

public interface JPAAsignacionEvaluadorRepository extends JpaRepository<AssignEvaluatorEntity, Integer> {
}
