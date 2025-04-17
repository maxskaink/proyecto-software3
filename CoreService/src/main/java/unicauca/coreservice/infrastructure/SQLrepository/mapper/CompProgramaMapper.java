package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.CompetenciaPrograma;
import unicauca.coreservice.infrastructure.SQLrepository.entity.CompetenciaProgramaEntity;

public class CompProgramaMapper {
    public static CompetenciaPrograma toCompPrograma(CompetenciaProgramaEntity entity) {
        if(entity == null)
            return null;
        return new CompetenciaPrograma(
                entity.getId(),
                entity.getDescripcion(),
                entity.getNivel(),
                RAProgramaMapper.toRAPrograma(entity.getResultadosAprendizaje())
        );
    }

    public static CompetenciaProgramaEntity toCompProgramaEntity(CompetenciaPrograma comp) {
        if (comp == null)
            return null;
        return new CompetenciaProgramaEntity(
                comp.getId(),
                comp.getDescripcion(),
                comp.getNivel(),
                RAProgramaMapper.toRAProgramaEntity(comp.getRaPrograma()),
                true
        );
    }
}
