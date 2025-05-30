package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.CompetencyToSubjectAssignment;
import unicauca.coreservice.domain.model.OptionalWrapper;

public interface CompetencyToSubjectAssignmentRepositoryOutInt {

    /**
     * Adds a new CompetencyToSubjectAssignment to the repository.
     *
     * @param newCompetencyToSubject The CompetencyToSubjectAssignment to be added.
     * @return An {@code OptionalWrapper} containing the added CompetencyToSubjectAssignment if successful,
     *         or an exception if the operation fails.
     */
    OptionalWrapper<CompetencyToSubjectAssignment> add(CompetencyToSubjectAssignment newCompetencyToSubject);

    /**
     * Find a CompetencyToSubjectAssignment by the given ID.
     *
     * @param competencyId the unique identifier of the Competency whose associated CompetencyToSubjectAssignment is to be deleted
     * @param inActiveTerm
     * @return an OptionalWrapper containing the deleted CompetencyToSubjectAssignment if successful,
     * or an exception if the operation fails
     */
    OptionalWrapper<CompetencyToSubjectAssignment> getByCompetencyId(Integer competencyId, boolean inActiveTerm);

    /**
     * Find a CompetencyToSubjectAssignment by the given ID.
     * @param id id of the assigment
     * @return the instance of CompetencyToSubjectAssignment identified by the given ID, or an empty OptionalWrapper if not found.
     */
    OptionalWrapper<CompetencyToSubjectAssignment> getById(Integer id);
    /**
     * Deletes a CompetencyToSubjectAssignment entity identified by the specified ID.
     *
     * @param CompetencyToSubjectId The unique identifier of the CompetencyToSubjectAssignment to be deleted.
     * @return An {@code OptionalWrapper} containing the deleted CompetencyToSubjectAssignment if the operation is successful,
     *         or an exception if the deletion fails.
     */
    OptionalWrapper<CompetencyToSubjectAssignment> remove(Integer CompetencyToSubjectId);
}
