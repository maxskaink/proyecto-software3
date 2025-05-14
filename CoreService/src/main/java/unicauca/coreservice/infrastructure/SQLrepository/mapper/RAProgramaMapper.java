package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.ProgramOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RAProgramaEntity;

public class RAProgramaMapper {
    public static ProgramOutcome toRAPrograma(RAProgramaEntity entity){
        if(entity == null)
            return null;
        return new ProgramOutcome(
                entity.getId(),
                entity.getDescripcion()
        );
    }

    /**
     * WARNING: It can lose the reference to the competence
     * @param programa the instance of ProgramOutcome
     * @return The RAProgramaEntity
     */
    public static RAProgramaEntity toRAProgramaEntity(ProgramOutcome programa){
        if(programa == null)
            return null;
        return new RAProgramaEntity(
                programa.getId(),
                programa.getDescription(),
                null,
                true
        );
    }
}
