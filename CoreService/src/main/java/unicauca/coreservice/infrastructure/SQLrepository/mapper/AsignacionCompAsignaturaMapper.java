package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.AssignCompetencyToSubject;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;

public class AsignacionCompAsignaturaMapper {

    public static AssignCompetencyToSubject toAsignacionCompAsignatura(AsignacionCompetenciaAsignaturaEntity entity){
        return null==entity?null:
                new AssignCompetencyToSubject(
                        entity.getId(),
                        CompAsignaturaMapper.toCompAsignatura(entity.getCompetencia()),
                        AsignaturaMapper.toAsignatura(entity.getAsignatura()),
                        PeriodoMapper.toPeriodo(entity.getPeriodo()),
                        entity.getRAAsignaturas().stream().map(RAAsignaturaMapper::toRAAsignatura).toList()
                );
    }
}
