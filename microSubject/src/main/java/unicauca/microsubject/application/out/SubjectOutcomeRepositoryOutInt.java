package unicauca.microsubject.application.out;

import unicauca.microsubject.domain.model.OptionalWrapper;
import unicauca.microsubject.domain.model.SubjectOutcome;

public interface SubjectOutcomeRepositoryOutInt {

    /**
     * Retrieves a SubjectOutcome by its unique identifier.
     *
     * @param id The ID of the SubjectOutcome to retrieve.
     * @return An {@link OptionalWrapper} containing the found SubjectOutcome or an exception if not found.
     */
    OptionalWrapper<SubjectOutcome> getById(Integer id);

}
