package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaAsignaturaEntity;

import java.util.stream.Collectors;

public class CompAsignaturaMapper {
    public static CompetenciaAsignatura toCompAsignatura(CompetenciaAsignaturaEntity entity){
        return null==entity?null:
                new CompetenciaAsignatura(
                    entity.getId(),
                        entity.getDescripcion(),
                        entity.getNivel(),
                        entity.getCompetenciaPrograma().getId()
                );
    }
    public static CompetenciaAsignaturaEntity toEntity(CompetenciaAsignatura compAsignatura){
        return null==compAsignatura?null:
                new CompetenciaAsignaturaEntity(
                        compAsignatura.getId(),
                        compAsignatura.getDescripcion(),
                        compAsignatura.getNivel(),
                        null,
                        true
                );
    }
}
