package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.EvaluatorAssignmentEntity;

public interface JPAEvaluatorAssignmentRepository extends JpaRepository<EvaluatorAssignmentEntity, Integer> {
}
