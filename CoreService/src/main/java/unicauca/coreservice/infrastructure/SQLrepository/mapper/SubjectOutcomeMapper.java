package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;

public class SubjectOutcomeMapper {
    public static SubjectOutcomeEntity toSubjectOutcomeEntity(SubjectOutcome subjectOutcome) {
        return null==subjectOutcome? null:
                new SubjectOutcomeEntity(
                        subjectOutcome.getId(),
                        subjectOutcome.getDescription(),
                        null,
                        true
                );
    }

    public static SubjectOutcome toSubjectOutcome(SubjectOutcomeEntity subjectOutcomeEntity){
        return null==subjectOutcomeEntity?null:
                new SubjectOutcome(
                        subjectOutcomeEntity.getId(),
                        subjectOutcomeEntity.getDescription()
                );
    }
}
