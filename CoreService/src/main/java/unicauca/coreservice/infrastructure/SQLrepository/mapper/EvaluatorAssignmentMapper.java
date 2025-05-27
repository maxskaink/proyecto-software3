package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.EvaluatorAssignment;
import unicauca.coreservice.infrastructure.SQLrepository.entity.EvaluatorAssignmentEntity;

/**
 * Mapper class to convert between EvaluatorAssignment and EvaluatorAssignmentEntity.
 * This class provides methods to map domain model objects to database entities
 * and vice versa.
 * CAUTION: This mapper does not handle the Term and Subject fields,
 * as they are not included in the EvaluatorAssignmentEntity.
 * If you need to handle these fields, you should implement additional logic
 * to fetch or set them as needed.
 */
public class EvaluatorAssignmentMapper {
    public static EvaluatorAssignmentEntity toEvaluatorAssignmentEntity(EvaluatorAssignment evaluatorAssignment){
        return null == evaluatorAssignment ? null :
                new EvaluatorAssignmentEntity(
                        evaluatorAssignment.getId(),
                        null,
                        null,
                        evaluatorAssignment.getEvaluatorUid()
                );
    }

    public static EvaluatorAssignment toEvaluatorAssignment(EvaluatorAssignmentEntity evaluatorAssignmentEntity){
        return null == evaluatorAssignmentEntity ? null:
                new EvaluatorAssignment(
                        evaluatorAssignmentEntity.getId(),
                        null,
                        null,
                        evaluatorAssignmentEntity.getEvaluatorUid()
                );
    }
}
