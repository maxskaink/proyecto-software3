package unicauca.coreservice.infrastructure.SQLrepository.JPARepository;

import org.springframework.data.jpa.repository.JpaRepository;
import unicauca.coreservice.infrastructure.SQLrepository.entity.EvaluatorAssignmentEntity;

import java.util.List;

public interface JPAEvaluatorAssignmentRepository extends JpaRepository<EvaluatorAssignmentEntity, Integer> {
    List<EvaluatorAssignmentEntity> findAllByEvaluatorId(Integer evaluatorId);
}
