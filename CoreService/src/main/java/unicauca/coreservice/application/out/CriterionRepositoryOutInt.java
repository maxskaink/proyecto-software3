package unicauca.coreservice.application.out;

import java.util.List;

import unicauca.coreservice.domain.model.Criterion;
import unicauca.coreservice.domain.model.OptionalWrapper;


public interface CriterionRepositoryOutInt {
    
    /**
     * Adds a new Criterion to the Repository
     *
     * @param newCriterion The instance of the new Criterion to be added
     * @return an OptionalWrapper containing the added Criterion or the Exception if it fails
     */
    OptionalWrapper<Criterion> add(Criterion newCriterion);

    /**
     * Retrieves a list of all available Criterion instances
     *
     * @return a List of Criterion instances or an Empty List if no Criterion instances are available
     */
    List<Criterion> listAll();

    /**
     * Retrieves a Criterion by its unique identifier
     *
     * @param id Identifier of the criterion instance
     * @return an OptionalWrapper containing the found Criterion or the Exception if none is found
     */
    OptionalWrapper<Criterion> getById(Integer id);

    /**
     * Retrieves a Criterion by the identifier of the rubric linked
     *
     * @param rubricId Identifier of the rubric linked
     * @return an OptionalWrapper containing the found Criterion or the Exception if none is found
     */
    List<Criterion> listByRubricId(Integer rubricId);

    /**
     * Updates a Criterion in the repository
     * 
     * @param id Identifier of the criterion instance
     * @param newRubric The new Rubric instance to be updated
     * @return an OptionalWrapper containing the updated Rubric or the Exception if it fails
     */
    OptionalWrapper<Criterion> update(Integer id, Criterion newCriterion);

    /**
     * Removes a Criterion from the repository by its unique id
     *
     * @param id Identifier of the criterion instance
     * @return an OptionalWrapper indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<Criterion> remove(Integer id);

}
