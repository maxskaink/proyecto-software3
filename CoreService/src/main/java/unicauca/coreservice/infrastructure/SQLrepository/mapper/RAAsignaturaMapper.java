package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectOutcomeEntity;

public class RAAsignaturaMapper {
    public static SubjectOutcomeEntity toEntity(SubjectOutcome ra) {
        return null==ra? null:
                new SubjectOutcomeEntity(
                        ra.getId(),
                        ra.getDescription(),
                        null,
                        true
                );
    }

    public static SubjectOutcome toRAAsignatura(SubjectOutcomeEntity entity){
        return null==entity?null:
                new SubjectOutcome(
                        entity.getId(),
                        entity.getDescription()
                );
    }
}
