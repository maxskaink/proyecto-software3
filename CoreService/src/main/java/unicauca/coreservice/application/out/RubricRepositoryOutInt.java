package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.Rubric;

import java.util.List;

public interface RubricRepositoryOutInt {
    /**
     * Adds a new Rubric to the Repository
     *
     * @param newRubric The instance of the new Rubric to be added
     * @return an OptionalWrapper containing the added Rubric or the Exception if it fails
     */
    OptionalWrapper<Rubric> add(Rubric newRubric);

    /**
     * Retrieves a list of all available Rubric instances in the subject
     *
     * @return a List of Rubric instances or an Empty List if no Rubric instances are available
     */
    List<Rubric> listAllBySubjectId(Integer subjectId);

    /**
     * Retrieves a Rubric by its unique identifier
     *
     * @param id Identifier of the rubric instance
     * @return an OptionalWrapper containing the found Rubric or the Exception if none is found
     */
    OptionalWrapper<Rubric> getById(Integer id);

    /**
     * Retrieves a Rubric by the identifier of the subject outcome linked
     *
     * @param subjectOutcomeId Identifier of the subject outcome linked
     * @return an OptionalWrapper containing the found Rubric or the Exception if none is found
     */
    OptionalWrapper<Rubric> getBySubjectOutcomeId(Integer subjectOutcomeId);

    /**
     * Updates a Rubric in the repository
     * 
     * @param id Identifier of the rubric instance
     * @param newRubric The new Rubric instance to be updated
     * @return an OptionalWrapper containing the updated Rubric or the Exception if it fails
     */
    OptionalWrapper<Rubric> update(Integer id, Rubric newRubric);

    /**
     * Removes a Rubric from the repository by its unique id
     *
     * @param id Identifier of the rubric instance
     * @return an OptionalWrapper indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<Rubric> remove(Integer id);
}
