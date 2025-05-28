package unicauca.microsubject.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.microsubject.infrastructure.SQLrepository.entity.EvaluatorAssignmentEntity;


public interface JPAEvaluatorAssignmentRepository extends JpaRepository<EvaluatorAssignmentEntity, Integer> {
}
