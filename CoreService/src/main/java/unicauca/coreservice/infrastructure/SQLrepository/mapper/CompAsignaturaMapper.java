package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyEntity;

public class CompAsignaturaMapper {
    public static SubjectCompetency toCompAsignatura(SubjectCompetencyEntity entity){
        return null==entity?null:
                new SubjectCompetency(
                    entity.getId(),
                        entity.getDescription(),
                        entity.getLevel(),
                        entity.getProgramCompetency().getId()
                );
    }
    public static SubjectCompetencyEntity toEntity(SubjectCompetency compAsignatura){
        return null==compAsignatura?null:
                new SubjectCompetencyEntity(
                        compAsignatura.getId(),
                        compAsignatura.getDescription(),
                        compAsignatura.getLevel(),
                        null,
                        true
                );
    }
}
