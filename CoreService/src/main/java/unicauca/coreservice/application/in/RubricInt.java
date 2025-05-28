package unicauca.coreservice.application.in;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import unicauca.coreservice.domain.model.Rubric;

@Validated
public interface RubricInt {
    
    /**
     * Add a new rubric to the system.
     *
     * @param newRubric the new rubric to be added
     * @param subjectOutcomeId the ID of the subject outcome associated with the rubric
     * @param uid uid of the user to validate if it has access
     * @return the added rubric
     * @throws Exception if an error occurs while adding the rubric
     */
    Rubric add(
            @NotNull(message = "The rubric can not be null")
            Rubric newRubric,
            @NotNull(message = "The subject outcome can not be null")
            Integer subjectOutcomeId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * List all rubrics in the system linked to the subject.
     * @param uid uid of the user to validate if it has access
     * @return a list of all rubrics
     */

    List<Rubric> listAllBySubjectId(
            @NotNull(message = "The subject id can not be null")
            Integer subjectId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Get a rubric by its ID.
     *
     * @param id the ID of the rubric
     * @param uid uid of the user to validate if it has access
     * @return the rubric with the specified ID
     * @throws Exception if an error occurs while retrieving the rubric
     */
    Rubric getById(
            @NotNull(message = "The rubric can not be null")
            Integer id,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Get a rubric by the ID of the subject outcome associated with it.
     *
     * @param subjectOutcomeId the ID of the subject outcome
     * @param uid uid of the user to validate if it has access
     * @return the rubric associated with the specified subject outcome ID
     * @throws Exception if an error occurs while retrieving the rubric
     */
    Rubric getBySubjectOutcomeId(
            @NotNull(message = "The subject outcome can not be null")
            Integer subjectOutcomeId,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;

    /**
     * Update an existing rubric.
     *
     * @param id the ID of the rubric to be updated
     * @param newRubric the new rubric data
     * @param uid uid of the user to validate if it has access
     * @return the updated rubric
     * @throws Exception if an error occurs while updating the rubric
     */
    Rubric update(@NotNull(message = "The rubric can not be null")
                  Integer id,
                  @NotNull(message = "The rubric can not be null")
                  Rubric newRubric,
                  @NotNull(message = "The uid can not be null")
                  String uid
    ) throws Exception;

    /**
     * Remove a rubric by its ID.
     *
     * @param id the ID of the rubric to be removed
     * @param uid uid of the user to validate if it has access
     * @return the removed rubric
     * @throws Exception if an error occurs while removing the rubric
     */
    Rubric remove(
            @NotNull(message = "The rubric can not be null")
            Integer id,
            @NotNull(message = "The uid can not be null")
            String uid
    ) throws Exception;
}
