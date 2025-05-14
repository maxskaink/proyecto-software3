package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.AssignCompetencyToSubject;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AssignSubjectCompetencyEntity;

public class AsignacionCompAsignaturaMapper {

    public static AssignCompetencyToSubject toAsignacionCompAsignatura(AssignSubjectCompetencyEntity entity){
        return null==entity?null:
                new AssignCompetencyToSubject(
                        entity.getId(),
                        CompAsignaturaMapper.toCompAsignatura(entity.getCompetency()),
                        AsignaturaMapper.toAsignatura(entity.getSubject()),
                        PeriodoMapper.toPeriodo(entity.getTerm()),
                        entity.getSubjectOutcomes().stream().map(RAAsignaturaMapper::toRAAsignatura).toList()
                );
    }
}
