package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.infrastructure.SQLrepository.entity.SubjectCompetencyAssignmentEntity;

public class AsignacionCompAsignaturaMapper {

    public static CompetencyToSubjectAssignment toAsignacionCompAsignatura(SubjectCompetencyAssignmentEntity entity){
        return null==entity?null:
                new CompetencyToSubjectAssignment(
                        entity.getId(),
                        CompAsignaturaMapper.toCompAsignatura(entity.getCompetency()),
                        AsignaturaMapper.toAsignatura(entity.getSubject()),
                        PeriodoMapper.toPeriodo(entity.getTerm()),
                        entity.getSubjectOutcomes().stream().map(RAAsignaturaMapper::toRAAsignatura).toList()
                );
    }
}
