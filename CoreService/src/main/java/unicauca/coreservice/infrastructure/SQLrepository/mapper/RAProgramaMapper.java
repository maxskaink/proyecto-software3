package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.ProgramOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramOutcomeEntity;

public class RAProgramaMapper {
    public static ProgramOutcome toRAPrograma(ProgramOutcomeEntity entity){
        if(entity == null)
            return null;
        return new ProgramOutcome(
                entity.getId(),
                entity.getDescription()
        );
    }

    /**
     * WARNING: It can lose the reference to the competence
     * @param programa the instance of ProgramOutcome
     * @return The ProgramOutcomeEntity
     */
    public static ProgramOutcomeEntity toRAProgramaEntity(ProgramOutcome programa){
        if(programa == null)
            return null;
        return new ProgramOutcomeEntity(
                programa.getId(),
                programa.getDescription(),
                null,
                true
        );
    }
}
