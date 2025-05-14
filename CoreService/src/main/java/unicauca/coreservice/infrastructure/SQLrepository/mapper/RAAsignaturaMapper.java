package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAAsignaturaEntity;

public class RAAsignaturaMapper {
    public static RAAsignaturaEntity toEntity(SubjectOutcome ra) {
        return null==ra? null:
                new RAAsignaturaEntity(
                        ra.getId(),
                        ra.getDescription(),
                        null,
                        true
                );
    }

    public static SubjectOutcome toRAAsignatura(RAAsignaturaEntity entity){
        return null==entity?null:
                new SubjectOutcome(
                        entity.getId(),
                        entity.getDescripcion()
                );
    }
}
