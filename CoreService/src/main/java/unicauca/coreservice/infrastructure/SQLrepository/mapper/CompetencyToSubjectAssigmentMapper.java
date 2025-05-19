package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;

import java.util.List;

public class CompetencyToSubjectAssigmentMapper {

    public static CompetencyToSubjectAssignment toSubjectCompetencyAssignment(SubjectCompetencyAssignmentEntity entity){
        return null==entity?null:
                new CompetencyToSubjectAssignment(
                        entity.getId(),
                        SubjectCompetencyMapper.toSubjectCompetency(entity.getCompetency()),
                        SubjectMapper.toSubject(entity.getSubject()),
                        TermMapper.toTerm(entity.getTerm()),
                        entity.getSubjectOutcomes() == null ? List.of() : entity.getSubjectOutcomes().stream().map(SubjectOutcomeMapper::toSubjectOutcome).toList()
                );
    }
}
