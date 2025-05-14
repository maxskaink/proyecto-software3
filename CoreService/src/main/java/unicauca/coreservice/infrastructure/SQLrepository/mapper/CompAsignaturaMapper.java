package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;

public class CompAsignaturaMapper {
    public static SubjectCompetency toCompAsignatura(CompetenciaAsignaturaEntity entity){
        return null==entity?null:
                new SubjectCompetency(
                    entity.getId(),
                        entity.getDescripcion(),
                        entity.getNivel(),
                        entity.getCompetenciaPrograma().getId()
                );
    }
    public static CompetenciaAsignaturaEntity toEntity(SubjectCompetency compAsignatura){
        return null==compAsignatura?null:
                new CompetenciaAsignaturaEntity(
                        compAsignatura.getId(),
                        compAsignatura.getDescription(),
                        compAsignatura.getLevel(),
                        null,
                        true
                );
    }
}
