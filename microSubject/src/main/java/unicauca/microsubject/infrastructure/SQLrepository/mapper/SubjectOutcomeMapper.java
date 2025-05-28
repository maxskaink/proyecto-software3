package unicauca.microsubject.infrastructure.SQLrepository.mapper;

import unicauca.microsubject.domain.model.SubjectOutcome;
import unicauca.microsubject.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;

public class SubjectOutcomeMapper {
    public static SubjectOutcomeEntity toSubjectOutcomeEntity(SubjectOutcome subjectOutcome) {
        if (null == subjectOutcome) { return null; }
        return new SubjectOutcomeEntity(
                subjectOutcome.getId(),
                subjectOutcome.getDescription(),
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
