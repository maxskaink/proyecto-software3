package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.RAPrograma;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAProgramaEntity;

public class RAProgramaMapper {
    public static RAPrograma toRAPrograma(RAProgramaEntity entity){
        if(entity == null)
            return null;
        return new RAPrograma(
                entity.getId(),
                entity.getDescripcion()
        );
    }

    /**
     * WARNING: It can lose the reference to the competence
     * @param programa the instance of RAPrograma
     * @return The RAProgramaEntity
     */
    public static RAProgramaEntity toRAProgramaEntity(RAPrograma programa){
        if(programa == null)
            return null;
        return new RAProgramaEntity(
                programa.getId(),
                programa.getDescripcion(),
                null,
                true
        );
    }
}
