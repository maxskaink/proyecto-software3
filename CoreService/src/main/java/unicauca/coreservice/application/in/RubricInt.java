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
     * @return the added rubric
     * @throws Exception if an error occurs while adding the rubric
     */
    Rubric add(@NotNull(message = "The rubric can not be null") Rubric newRubric, 
                @NotNull(message = "The subject outcome can not be null") Integer subjectOutcomeId) throws Exception;

    /**
     * List all rubrics in the system linked to the subject.
     *
     * @return a list of all rubrics
     */

    List<Rubric> listAllBySubjectId(Integer subjectId);

    /**
     * Get a rubric by its ID.
     *
     * @param id the ID of the rubric
     * @return the rubric with the specified ID
     * @throws Exception if an error occurs while retrieving the rubric
     */
    Rubric getById(@NotNull(message = "The rubric can not be null") Integer id) throws Exception;

    /**
     * Get a rubric by the ID of the subject outcome associated with it.
     *
     * @param subjectOutcomeId the ID of the subject outcome
     * @return the rubric associated with the specified subject outcome ID
     * @throws Exception if an error occurs while retrieving the rubric
     */
    Rubric getBySubjectOutcomeId(@NotNull(message = "The subject outcome can not be null") Integer subjectOutcomeId) throws Exception;

    /**
     * Update an existing rubric.
     *
     * @param id the ID of the rubric to be updated
     * @param newRubric the new rubric data
     * @return the updated rubric
     * @throws Exception if an error occurs while updating the rubric
     */
    Rubric update(@NotNull(message = "The rubric can not be null") Integer id, 
                  @NotNull(message = "The rubric can not be null") Rubric newRubric) throws Exception;

    /**
     * Remove a rubric by its ID.
     *
     * @param id the ID of the rubric to be removed
     * @return the removed rubric
     * @throws Exception if an error occurs while removing the rubric
     */
    Rubric remove(@NotNull(message = "The rubric can not be null") Integer id) throws Exception;
}
