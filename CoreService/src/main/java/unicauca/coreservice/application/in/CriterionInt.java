package unicauca.coreservice.application.in;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import unicauca.coreservice.domain.model.Criterion;

public interface CriterionInt {

    /**
     * Add a new criterion to the system.
     *
     * @param newCriterion the new criterion to be added
     * @param rubricId the ID of the rubric associated with the criterion
     * @param uid uid of the user to validate if it has access.
     * @return the added criterion
     * @throws Exception if an error occurs while adding the criterion
     */
    Criterion add(
            @NotNull(message = "Criterion can not be null")
            Criterion newCriterion,
            @NotNull(message = "Rubric id can not be null")
            Integer rubricId,
            @NotNull(message ="uid can not be null")
            String uid
    ) throws Exception;


    /**
     * List all criteria in the rubric.
     *  
     * @param rubricId the ID of the rubric associated with the criteria
     * @param uid uid of the user to validate if it has access.
     * @return a list of all criteria
     */

    List<Criterion> listAllByRubricId(
            @NotNull(message = "Rubric Id can not be null")
            Integer rubricId,
            @NotNull(message ="uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Get a criterion by its ID.
     *
     * @param id the ID of the criterion
     * @param uid uid of the user to validate if it has access.
     * @return the criterion with the specified ID
     * @throws Exception if an error occurs while retrieving the criterion
     */
    Criterion getById(
            @NotNull(message = "Criterion id can not be null")
            Integer id,
            @NotNull(message ="uid can not be null")
            String uid
    ) throws Exception;

   
    /**
     * Update an existing criterion.
     *
     * @param id the ID of the criterion to be updated
     * @param uid uid of the user to validate if it has access.
     * @param newCriterion the new criterion data
     * @return the updated criterion
     * @throws Exception if an error occurs while updating the criterion
     */
    Criterion update(
            @NotNull(message = "Criterion id can not be null")
            Integer id,
            @NotNull(message = "Criterion can not be null")
            Criterion newCriterion,
            @NotNull(message ="uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Delete a criterion by its ID.
     * @param id the ID of the criterion to be deleted
     * @param uid uid of the user to validate if it has access.
     * @return the deleted criterion
     * @throws Exception if an error occurs while deleting the criterion
     */
    Criterion remove(
            @NotNull(message = "Criterion id can not be null")
            Integer id,
            @NotNull(message ="uid can not be null")
            String uid
    ) throws Exception;

    
}
