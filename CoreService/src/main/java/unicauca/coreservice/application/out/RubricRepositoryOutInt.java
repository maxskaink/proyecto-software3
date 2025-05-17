package unicauca.coreservice.application.out;

import org.springframework.data.repository.Repository;
import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.infrastructure.SQLrepository.entity.RubricEntity;

import java.util.List;

public interface RubricRepositoryOutInt {
    /**
     * Adds a new Rubric to the Repository
     *
     * @param newRubric The instance of the new Rubric to be added
     * @return an OptionalWrapper containing the added Rubric or the Exception if it fails
     */
    OptionalWrapper<RubricEntity> add(RubricEntity newRubric);

    /**
     * Retrieves a list of all available Rubric instances
     *
     * @return a List of Rubric instances or an Empty List if no Rubric instances are available
     */
    List<RubricEntity> listAll();

    /**
     * Retrieves a RubricEntity by its unique identifier
     *
     * @param id Identifier of the rubric instance
     * @return an OptionalWrapper containing the found Rubric or the Exception if none is found
     */
    OptionalWrapper<RubricEntity> getById(Integer id);

    /**
     * Retrieves a RubricEntity by the identifier of the subject outcome linked
     *
     * @param subjectOutcomeId Identifier of the subject outcome linked
     * @return an OptionalWrapper containing the found Rubric or the Exception if none is found
     */
    OptionalWrapper<RubricEntity> getBySubjectOutcomeId(Integer subjectOutcomeId);


    /**
     * Removes a RubricEntity from the repository by its unique id
     *
     * @param id Identifier of the rubric instance
     * @return an OptionalWrapper indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<RubricEntity> remove(Integer id);
}
