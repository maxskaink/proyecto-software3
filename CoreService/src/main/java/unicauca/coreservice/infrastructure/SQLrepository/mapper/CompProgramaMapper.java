package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;

import java.util.stream.Collectors;

public class CompProgramaMapper {
    public static ProgramCompetency toCompPrograma(CompetenciaProgramaEntity entity) {
        if(entity == null)
            return null;
        return new ProgramCompetency(
                entity.getId(),
                entity.getDescripcion(),
                entity.getNivel(),
                RAProgramaMapper.toRAPrograma(entity.getResultadosAprendizaje()),
                entity.getCompetenciasAsignatura().stream()
                        .map(CompAsignaturaMapper::toCompAsignatura)
                        .collect(Collectors.toList())
        );
    }

    public static CompetenciaProgramaEntity toCompProgramaEntity(ProgramCompetency comp) {
        if (comp == null)
            return null;
        return new CompetenciaProgramaEntity(
                comp.getId(),
                comp.getDescription(),
                comp.getLevel(),
                comp.getSubjectCompetency().stream()
                        .map(CompAsignaturaMapper::toEntity)
                        .collect(Collectors.toList()),
                RAProgramaMapper.toRAProgramaEntity(comp.getProgramOutcome()),
                true
        );
    }
}
