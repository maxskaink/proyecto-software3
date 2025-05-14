package unicauca.coreservice.application.out;

import unicauca.coreservice.domain.model.SubjectCompetency;
import unicauca.coreservice.domain.model.OptionalWrapper;

import java.util.List;

public interface SubjectCompetencyRepositoryOutInt {
    /**
     * Adds a new SubjectCompetency to the repository.
     *
     * @param newSubjectCompetency The SubjectCompetency to be added.
     * @return An {@link OptionalWrapper} containing the added SubjectCompetency or an exception if the process fails.
     */
    OptionalWrapper<SubjectCompetency> add(SubjectCompetency newSubjectCompetency);

    /**
     * Retrieves all SubjectCompetency entities from the repository.
     *
     * @return A {@link List} of all SubjectCompetency entities.
     */
    List<SubjectCompetency> listAll();

    /**
     * Retrieves a list of SubjectCompetency associated with a specific Subject.
     *
     * @param subjectId The unique identifier of the Subject whose associated SubjectCompetency entities are to be retrieved.
     * @return A list of SubjectCompetency objects associated with the specified Subject.
     */
    List<SubjectCompetency> listAllBySubjectId(Integer subjectId);
    /**
     * Retrieves a SubjectCompetency by its unique identifier.
     *
     * @param id The ID of the SubjectCompetency to retrieve.
     * @return An {@link OptionalWrapper} containing the found SubjectCompetency or an exception if not found.
     */
    OptionalWrapper<SubjectCompetency> getById(Integer id);

    /**
     * Updates an existing SubjectCompetency by its unique identifier.
     *
     * @param id                       The ID of the SubjectCompetency to updateProgramCompetency.
     * @param newSubjectCompetency The updated SubjectCompetency entity.
     * @return An {@link OptionalWrapper} containing the updated SubjectCompetency or an exception if the updateProgramCompetency fails.
     */
    OptionalWrapper<SubjectCompetency> update(Integer id, SubjectCompetency newSubjectCompetency);

    /**
     * Removes a SubjectCompetency from the repository by its unique identifier.
     *
     * @param id The ID of the SubjectCompetency to remove.
     * @return An {@link OptionalWrapper} indicating the outcome of the operation, or an exception if the removal fails.
     */
    OptionalWrapper<SubjectCompetency> remove(Integer id);


}
