package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.ProgramCompetency;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramCompetencyEntity;

import java.util.stream.Collectors;

public class ProgramCompetencyMapper {
    public static ProgramCompetency toProgramCompetency(ProgramCompetencyEntity entity) {
        if(entity == null)
            return null;
        return new ProgramCompetency(
                entity.getId(),
                entity.getDescription(),
                entity.getLevel(),
                ProgramOutcomeMapper.toProgramOutcome(entity.getLearningOutcomes()),
                entity.getSubjectCompetencies().stream()
                        .map(SubjectCompetencyMapper::toSubjectCompetency)
                        .collect(Collectors.toList())
        );
    }

    public static ProgramCompetencyEntity toProgramCompetencyEntity(ProgramCompetency comp) {
        if (comp == null)
            return null;
        return new ProgramCompetencyEntity(
                comp.getId(),
                comp.getDescription(),
                comp.getLevel(),
                comp.getSubjectCompetency().stream()
                        .map(SubjectCompetencyMapper::toSubjectCompetencyEntity)
                        .collect(Collectors.toList()),
                ProgramOutcomeMapper.toProgramOutcomeEntity(comp.getProgramOutcome()),
                true
        );
    }
}
