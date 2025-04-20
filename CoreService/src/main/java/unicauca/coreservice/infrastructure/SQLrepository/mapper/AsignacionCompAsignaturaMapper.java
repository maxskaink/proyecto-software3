package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.AsignacionCompetenciaAsignatura;
import unicauca.coreservice.domain.model.Asignatura;
import unicauca.coreservice.domain.model.CompetenciaAsignatura;
import unicauca.coreservice.infrastructure.SQLrepository.entity.AsignacionCompetenciaAsignaturaEntity;

public class AsignacionCompAsignaturaMapper {

    public static AsignacionCompetenciaAsignatura toAsignacionCompAsignatura(AsignacionCompetenciaAsignaturaEntity entity){
        return null==entity?null:
                new AsignacionCompetenciaAsignatura(
                        entity.getId(),
                        CompAsignaturaMapper.toCompAsignatura(entity.getCompetencia()),
                        AsignaturaMapper.toAsignatura(entity.getAsignatura()),
                        PeriodoMapper.toPeriodo(entity.getPeriodo())
                );
    }
}
