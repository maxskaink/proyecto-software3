package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;

import java.util.stream.Collectors;

public class CompProgramaMapper {
    public static ProgramCompetency toCompPrograma(ProgramCompetencyEntity entity) {
        if(entity == null)
            return null;
        return new ProgramCompetency(
                entity.getId(),
                entity.getDescription(),
                entity.getLevel(),
                RAProgramaMapper.toRAPrograma(entity.getLearningOutcomes()),
                entity.getSubjectCompetencies().stream()
                        .map(CompAsignaturaMapper::toCompAsignatura)
                        .collect(Collectors.toList())
        );
    }

    public static ProgramCompetencyEntity toCompProgramaEntity(ProgramCompetency comp) {
        if (comp == null)
            return null;
        return new ProgramCompetencyEntity(
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
