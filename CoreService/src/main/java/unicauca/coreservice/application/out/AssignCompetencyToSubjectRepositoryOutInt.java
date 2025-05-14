package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.AssignCompetencyToSubject;
import unicauca.coreservice.domain.model.OptionalWrapper;

public interface AssignCompetencyToSubjectRepositoryOutInt {

    /**
     * Adds a new AssignCompetencyToSubject to the repository.
     *
     * @param newCompetencyToSubject The AssignCompetencyToSubject to be added.
     * @return An {@code OptionalWrapper} containing the added AssignCompetencyToSubject if successful,
     *         or an exception if the operation fails.
     */
    OptionalWrapper<AssignCompetencyToSubject> add(AssignCompetencyToSubject newCompetencyToSubject);

    /**
     * Find an AssignCompetencyToSubject by the given Competencia ID.
     *
     * @param subjectId the unique identifier of the Competencia whose associated AssignCompetencyToSubject is to be deleted
     * @return an OptionalWrapper containing the deleted AssignCompetencyToSubject if successful,
     *         or an exception if the operation fails
     */
    OptionalWrapper<AssignCompetencyToSubject> getBySubjectId(Integer subjectId);

    /**
     * Deletes an AssignCompetencyToSubject entity identified by the specified ID.
     *
     * @param CompetencyToSubjectId The unique identifier of the AssignCompetencyToSubject to be deleted.
     * @return An {@code OptionalWrapper} containing the deleted AssignCompetencyToSubject if the operation is successful,
     *         or an exception if the deletion fails.
     */
    OptionalWrapper<AssignCompetencyToSubject> remove(Integer CompetencyToSubjectId);
}
