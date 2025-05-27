package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;

public class SubjectOutcomeMapper {
    public static SubjectOutcomeEntity toSubjectOutcomeEntity(SubjectOutcome subjectOutcome) {
        if (null == subjectOutcome) { return null; }
        RubricEntity rubricEntity = subjectOutcome.getRubric()==null?null:new RubricEntity();
        return new SubjectOutcomeEntity(
                subjectOutcome.getId(),
                subjectOutcome.getDescription(),
                null,
                rubricEntity,
                true
        );
    }

    public static SubjectOutcome toSubjectOutcome(SubjectOutcomeEntity subjectOutcomeEntity){
        return null==subjectOutcomeEntity?null:
                new SubjectOutcome(
                        subjectOutcomeEntity.getId(),
                        subjectOutcomeEntity.getDescription(),
                        RubricMapper.toRubric(subjectOutcomeEntity.getRubric()),
                        subjectOutcomeEntity.getCompetencyAssignment().getId()
                );
    }
}
