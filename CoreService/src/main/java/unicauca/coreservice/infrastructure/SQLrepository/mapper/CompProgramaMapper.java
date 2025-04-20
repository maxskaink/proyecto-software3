package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;

import java.util.stream.Collectors;

public class CompProgramaMapper {
    public static CompetenciaPrograma toCompPrograma(CompetenciaProgramaEntity entity) {
        if(entity == null)
            return null;
        return new CompetenciaPrograma(
                entity.getId(),
                entity.getDescripcion(),
                entity.getNivel(),
                RAProgramaMapper.toRAPrograma(entity.getResultadosAprendizaje()),
                entity.getCompetenciasAsignatura().stream()
                        .map(CompAsignaturaMapper::toCompAsignatura)
                        .collect(Collectors.toList())
        );
    }

    public static CompetenciaProgramaEntity toCompProgramaEntity(CompetenciaPrograma comp) {
        if (comp == null)
            return null;
        return new CompetenciaProgramaEntity(
                comp.getId(),
                comp.getDescripcion(),
                comp.getNivel(),
                comp.getCompetenciasAsignatura().stream()
                        .map(CompAsignaturaMapper::toEntity)
                        .collect(Collectors.toList()),
                RAProgramaMapper.toRAProgramaEntity(comp.getRaPrograma()),
                true
        );
    }
}
