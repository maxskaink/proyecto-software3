package unicauca.coreservice.infrastructure.SQLrepository.mapper;

import unicauca.coreservice.domain.model.ProgramOutcome;
import unicauca.coreservice.infrastructure.SQLrepository.entity.ProgramOutcomeEntity;

public class ProgramOutcomeMapper {
    public static ProgramOutcome toProgramOutcome(ProgramOutcomeEntity entity){
        if(entity == null)
            return null;
        return new ProgramOutcome(
                entity.getId(),
                entity.getDescription()
        );
    }

    /**
     * WARNING: It can lose the reference to the competence
     * @param program the instance of ProgramOutcome
     * @return The ProgramOutcomeEntity
     */
    public static ProgramOutcomeEntity toProgramOutcomeEntity(ProgramOutcome program){
        if(program == null)
            return null;
        return new ProgramOutcomeEntity(
                program.getId(),
                program.getDescription(),
                null,
                true
        );
    }
}
