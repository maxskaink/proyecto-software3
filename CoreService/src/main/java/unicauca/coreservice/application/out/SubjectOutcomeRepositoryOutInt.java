package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.OptionalWrapper;
import unicauca.coreservice.domain.model.SubjectOutcome;

import java.util.List;

public interface SubjectOutcomeRepositoryOutInt {

    /**
     * Adds a new SubjectOutcome to the repository associated with a specific competencyAssigned.
     *
     * @param NewSubjectOutcome The SubjectOutcome to be added.
     * @param competencyAssigned The identifier of the competencyAssigned to associate with the SubjectOutcome.
     * @return An {@code OptionalWrapper} containing the added SubjectOutcome if successful, or an exception if the operation fails.
     */
    OptionalWrapper<SubjectOutcome> add(SubjectOutcome NewSubjectOutcome, Integer competencyAssigned);

    /**
     * Retrieves a list of SubjectOutcome associated with a specific Subject.
     *
     * @param subjectId The unique identifier of the Subject whose associated SubjectOutcome entities are to be retrieved.
     * @param activeTerm If it is true, return all the SubjectOutcome in the active ter. If its doesn't return all the SubjectOutcome
     * @return A list of SubjectOutcome objects associated with the specified Subject.
     */
    List<SubjectOutcome> listAllBySubjectId(Integer subjectId, boolean activeTerm);

    /**
     * Retrieves a list of SubjectOutcome associated with a specific Subject and a specific Term
     * @param subjectId The unique identifier of the Subject whose associated with the Subject
     * @param idTerm The unique identifier of the Subject whose associated with the Subject
     * @return The list of SubjectOutcome objects associated with the specified Subject and Term
     */
    List<SubjectOutcome> listAllBySubjectIdAndTermId(Integer subjectId, Integer idTerm);

    /**
     * Retrieves a list of SubjectOutcome associated with a specific Competency.
     *
     * @param competencyId The unique identifier of the Competency.
     * @return A list of SubjectOutcome objects associated with the specified Competency.
     */
    List<SubjectOutcome> listAllByCompetencyId(Integer competencyId);
    /**
     * Retrieves a SubjectOutcome by its unique identifier.
     *
     * @param id The ID of the SubjectOutcome to retrieve.
     * @return An {@link OptionalWrapper} containing the found SubjectOutcome or an exception if not found.
     */
    OptionalWrapper<SubjectOutcome> getById(Integer id);

    /**
     * Updates an existing SubjectOutcome by its unique identifier.
     *
     * @param id              The ID of the SubjectOutcome to updateProgramCompetency.
     * @param NewSubjectOutcome The updated SubjectOutcome entity.
     * @return An {@link OptionalWrapper} containing the updated SubjectOutcome or an exception if the updateProgramCompetency fails.
     */
    OptionalWrapper<SubjectOutcome> update(Integer id, SubjectOutcome NewSubjectOutcome);

    /**
     * Removes a SubjectOutcome from the repository by its unique identifier.
     *
     * @param id The ID of the SubjectOutcome to remove.
     * @return An {@link OptionalWrapper} indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<SubjectOutcome> remove(Integer id);

}
